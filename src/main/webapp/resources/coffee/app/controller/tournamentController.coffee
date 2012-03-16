define [
  'lib/jquery'
  'lib/underscore'
  'lib/handlebars'
  'app/model/TournamentModel'
  'app/model/TeamModel'
  'app/view/TeamView'
  'app/model/ScoreModel'
  'app/view/ScoreView'
  'app/view/PercentCompleteView'
  'text!html/sectionTemplate.html'
  'text!html/gameTemplate.html'
  'text!html/scoreTemplate.html'
  'text!html/browserDetectTemplate.html'
], ($, _, handlebars, TournamentModel, TeamModel, TeamView, ScoreModel, ScoreView, PercentCompleteView, strSectionTemplate, strGameTemplate, strScoreTemplate,strBrowserDetectTemplate) ->

  init: ->
    @model = new TournamentModel
    @model.bind('change', @render, @)
    @model.fetch()
    @teamViews = {}
    @dropViews = []
    @sectionHB = handlebars.compile(strSectionTemplate)
    @gameHB = handlebars.compile(strGameTemplate)
    @browserDetectHB = handlebars.compile(strBrowserDetectTemplate)
    @percentView = new PercentCompleteView()

    if $.browser.msie and $.browser.version isnt '9.0'
      $(@browserDetectHB({})).dialog({
        title: 'The Bracket App'
        height: 200
        width: 600
        position: ['center',50]
        modal:true
        resizable:false
      })
      $('#ie-detect').focus()


  render: ->
    @pickStatus = @model.get('pickStatus')

    if @pickStatus isnt "OPEN"
      $('.navbar.leaderboard').removeClass('hidden')
#      $('#scoreboard').removeClass('hidden')

#    console.log("GET: http://#{window.location.host + @model.url()}\n\n")

    @scoreView = new ScoreView(
      model:new ScoreModel(tieBreaker:@model.get('tieBreaker'))
    )
    @scoreView.model.on('change',@updatePercent,@)

    elBracket = $(@sectionHB(
      class: "regions"
    ))

    @model.get('regions')?.forEach (region) =>
      elRegion = $(@sectionHB(
        class: "region region-#{region.regionId}"
        header:region.name
      ))

      region.rounds?.forEach (round) =>
        elRound = $(@sectionHB(
          class: "round round-#{round.roundId}"
          finalScoreSection: true if region.regionId is 5 and round.roundId is 3
        ))

        round.games?.forEach (game) =>
          game.finalVS = true if region.regionId is 5 and round.roundId is 2 and game.gameId is 3
          elGame = $(@gameHB(game))
          game.teams?.forEach (team) =>
            team.regionId = region.regionId
            team.roundId = round.roundId
            team.gameId = game.gameId
            team.nextGame = (if game.nextGame then game.nextGame else null)
            team.pickable = true if team.teamId
            team.locator = "#{region.regionId}-#{game.gameId}-#{team.position}"
            team.pickStatus = @pickStatus

            teamView = new TeamView(
              model:new TeamModel(team)
            )
            teamView.on('drag',@findShowDropViews,@)
            teamView.on('dragStop',@hideDropZones,@)
            teamView.on('drop',@teamDrop,@)
            teamView.on('advance',@advanceTeam,@)
            teamView.on('remove',@removeTeam,@)
            teamView.model.on('change',@updatePercent,@)

            @teamViews[team.locator] = teamView

            teamZero = elGame.find('.detail.team-0')
            finalVS = elGame.find('.detail.finalVS')
            if teamZero.html()
              if finalVS.html()
                teamView.$el.insertAfter(finalVS)
              else
                teamView.$el.insertAfter(teamZero)
            else
              teamView.$el.insertAfter(elGame.find('.detail.state'))

          elRound.append(elGame)
          elRound.append(@scoreView.$el) if region.regionId is 5 and round.roundId is 3
        elRegion.append(elRound)
      elBracket.append(elRegion)

    @updatePercent()
    $('#bracketNode').append(elBracket)

  updatePercent: ->
    if @pickStatus is "OPEN"
      @percentView.render(@teamViews,@scoreView)


  findShowDropViews: (baseView) ->
    @dropViews = @recurNextTeamViews(baseView,'showDropZone')

  hideDropZones:(baseView) ->
    if not @dropViews
      @dropViews = @recurNextTeamViews(baseView,'hideDropZone')
    else
      @dropViews.forEach (view) ->
        view.hideDropZone()

  advanceTeam:(landingView) ->
    return if not (landingView?.model.get('teamId') or landingView?.model.get('userPick')?.teamId)
    nextGame = landingView?.model.get('nextGame')
    baseView = @teamViews["#{nextGame.regionId}-#{nextGame.gameId}-#{nextGame.position}"]
    pendingSaveArr = []
    if landingView and baseView
      pendingSaveArr.push(
        view:baseView
        model:
          userPick:
            name:(landingView.model.get('name') or landingView.model.get('userPick')?.name)
            teamId:(landingView.model.get('teamId') or landingView.model.get('userPick')?.teamId)
            seed:(landingView.model.get('seed') or landingView.model.get('userPick')?.seed)
            regionId:landingView.model.get('regionId')
          previousGame:
            regionId: landingView.model.get('regionId'),
            gameId: landingView.model.get('gameId'),
            position: landingView.model.get('position')
      )
      @chainSaveCallbacks(pendingSaveArr, @checkRemoveFutureWins,[baseView,baseView.model.get('previousGame')])




  removeTeam:(startingView) ->
    teamId = startingView?.model.get('userPick')?.teamId
    return if not teamId

    deleteCallbackArr = [startingView]
    nextTeamViewArr = @recurNextTeamViews(startingView)

    nextTeamViewArr.forEach((view) ->
      if view.model.get('userPick')?.teamId is teamId
        deleteCallbackArr.push(view)
    )

    @chainDeleteCallbacks(deleteCallbackArr)


  teamDrop: (baseView,ui,landingView) ->
    postError = false
    pendingSaveArr = []

    if ui
      landingView = @teamViews["#{ui.draggable.data('locator')}"]

    if @validDropZone(baseView,landingView)
      previousGameView = landingView
      lastLandingView = _.find @dropViews, (dropView,i) =>
        eachView = @dropViews[i]

        pendingSaveArr.push(
          view:eachView
          model:
            userPick:
              name:(landingView.model.get('name') or landingView.model.get('userPick')?.name)
              teamId:(landingView.model.get('teamId') or landingView.model.get('userPick')?.teamId)
              seed:(landingView.model.get('seed') or landingView.model.get('userPick')?.seed)
              regionId:landingView.model.get('regionId')
            previousGame:
              regionId: previousGameView.model.get('regionId'),
              gameId: previousGameView.model.get('gameId'),
              position: previousGameView.model.get('position')
        )

        previousGameView = eachView

        _.isEqual baseView, dropView

      @chainSaveCallbacks(pendingSaveArr, @checkRemoveFutureWins,[lastLandingView,baseView.model.get('previousGame')])

  chainSaveCallbacks: (pendingSaveArr,callback,callbackArgs)->
    if @pickStatus isnt "OPEN"
      alert('Bracket picks have closed. Thanks for trying out the app, and check back soon to see your score!')
    else
      view = _.first(pendingSaveArr)?.view
      view?.$el.addClass('saving')
      callback?.apply(@,callbackArgs)

      view?.model.save(
        _.first(pendingSaveArr)?.model
      ,
        {
          wait:true
          success: (model,response) =>
  #          console.log("POST: http://#{window.location.host + model.url()}\nJSON:#{JSON.stringify(model.toJSON())}\n\n")
            view.$el.removeClass('saving')
            if pendingSaveArr.length > 1 #if there are more views
              @chainSaveCallbacks(_.last(pendingSaveArr,pendingSaveArr.length-1),callback,callbackArgs) #save the rest of the views
            else

          error: (model, response) =>
            pendingSaveArr.forEach((viewObj) ->
              viewObj.view.$el.removeClass('saving')
            )
            if response.status is 404
              alert 'Please sign in using twitter.'
  #          if response.status is 500
  #            console.log("POST ERROR: http://#{window.location.host + model.url()}\nJSON:#{JSON.stringify(model.toJSON())}\n\n")
        }
      )

  chainDeleteCallbacks: (pendingDeleteArr)->
    if @pickStatus isnt "OPEN"
      alert('Bracket picks have closed. Thanks for trying out the app, and check back soon to see your score!')
    else
      view = _.first(pendingDeleteArr)
      view?.$el.addClass('saving')

      view?.model.deletePick({
        success: () =>
          view.$el.removeClass('saving')
          view.model.set(
            userPick:null
            previousGame:null
          )

          if pendingDeleteArr.length > 1 #if there are more views
            @chainDeleteCallbacks(_.last(pendingDeleteArr,pendingDeleteArr.length-1))

        error: () =>
          pendingDeleteArr.forEach((view) ->
              view.$el.removeClass('saving')
          )
      })



  recurNextTeamViews: (baseView,actionAttr,actionAttrArgs,nextTeamViewArr) ->
    nextTeamView = null
    nextTeamViewArr = nextTeamViewArr or []
    nextGame = baseView?.model.get('nextGame')

    if nextGame
      nextTeamView = @teamViews["#{nextGame.regionId}-#{nextGame.gameId}-#{nextGame.position}"]

    if nextTeamView
      nextTeamViewArr.push(nextTeamView)
      nextTeamView[actionAttr]?.apply(nextTeamView, actionAttrArgs)

    if nextTeamView?.model.get('nextGame')
      @recurNextTeamViews(nextTeamView,actionAttr,actionAttrArgs,nextTeamViewArr)
    else
      nextTeamViewArr

  recurPreviousTeamViews: (previousGame,actionAttr,actionAttrArgs,previousTeamViewArr) ->
    previousTeamView = null
    previousTeamViewArr = previousTeamViewArr or []

    if previousGame
      previousTeamView = @teamViews["#{previousGame.regionId}-#{previousGame.gameId}-#{previousGame.position}"]

    if previousTeamView
      previousTeamView[actionAttr]?.apply(previousTeamView, actionAttrArgs)
      previousTeamViewArr.push(previousTeamView)
      @recurPreviousTeamViews(previousTeamView.model.get('previousGame'),actionAttr,actionAttrArgs,previousTeamViewArr)
    else
      previousTeamViewArr

  checkRemoveFutureWins: (baseView,previousGame) ->
    nextViewArr = @recurNextTeamViews(baseView)
    return if nextViewArr.length is 0

    pendingDeleteArr = []
    previousViewArr = @recurPreviousTeamViews(previousGame)
    if previousViewArr.length > 0
      firstView = _.first(previousViewArr)

      prevTeamId = (firstView.model.get('userPick')?.teamId or firstView.model.get('teamId'))
      nextViewArr.forEach((view) ->
          if (prevTeamId and (view.model.get('userPick')?.teamId is prevTeamId or view.model.get('teamId') is prevTeamId))
            pendingDeleteArr.push(view)
      )

      @chainDeleteCallbacks(pendingDeleteArr) if pendingDeleteArr.length > 0

  validDropZone: (baseView,landingView) ->
    return false if !landingView

    if @dropViews.length is 0
      @dropViews = @recurNextTeamViews(landingView)

    _.find @dropViews, (dropView) ->
      _.isEqual baseView, dropView