define [
  'lib/jquery'
  'lib/underscore'
  'lib/handlebars'
  'app/model/TournamentModel'
  'app/model/TeamModel'
  'app/view/TeamView'
  'app/model/ScoreModel'
  'app/view/ScoreView'
  'text!html/sectionTemplate.html'
  'text!html/gameTemplate.html'
  'text!html/scoreTemplate.html'
], ($, _, handlebars, TournamentModel, TeamModel, TeamView, ScoreModel, ScoreView, strSectionTemplate, strGameTemplate, strScoreTemplate) ->

  init: ->
    @model = new TournamentModel
    @model.bind('change', @render, @)
    @model.fetch()
    @teamViews = {}
    @dropViews = []
    @sectionHB = handlebars.compile(strSectionTemplate)
    @gameHB = handlebars.compile(strGameTemplate)


  render: ->
    if @model.get('pickStatus') isnt "OPEN"
      $('.navbar.leaderboard').removeClass('hidden')

    console.log("GET: http://#{window.location.host + @model.url()}\n\n")

    scoreView = new ScoreView(
      model:new ScoreModel(tieBreaker:@model.get('tieBreaker'))
    )

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

            teamView = new TeamView(
              model:new TeamModel(team)
            )
            teamView.on('drag',@findShowDropViews,@)
            teamView.on('dragStop',@hideDropZones,@)
            teamView.on('drop',@teamDrop,@)
            teamView.on('advance',@advanceTeam,@)
            teamView.on('remove',@removeTeam,@)

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
          elRound.append(scoreView.$el) if region.regionId is 5 and round.roundId is 3
        elRegion.append(elRound)
      elBracket.append(elRegion)

    $('#bracketNode').append(elBracket)


  findShowDropViews: (baseView) ->
    @dropViews = @recurNextTeamViews(baseView,'showDropZone')

  hideDropZones:(baseView) ->
    if not @dropViews
      @dropViews = @recurNextTeamViews(baseView,'hideDropZone')
    else
      @dropViews.forEach (view) ->
        view.hideDropZone()

  advanceTeam:(startingView) ->
    return if not startingView?.model.get('teamId')
    nextGame = startingView?.model.get('nextGame')
    endingView = @teamViews["#{nextGame.regionId}-#{nextGame.gameId}-#{nextGame.position}"]
    if startingView and endingView
      @chainSaveCallbacks([
        view:endingView
        model:
          name:startingView.model.get('name')
          teamId:startingView.model.get('teamId')
          seed:startingView.model.get('seed')
      ])
      @checkRemoveFutureWins(endingView)

  removeTeam:(startingView) ->
    return if not startingView?.model.get('teamId')

    @checkRemoveFutureWins(startingView)
    startingView.model.set(
      name:null
      teamId:null
      seed:null
    )

  teamDrop: (baseView,ui) ->
    postError = false
    pendingSaveArr = []

    landingView = @teamViews["#{ui.draggable.data('locator')}"]

    if @validDropZone(baseView,landingView)
      lastLandingView = _.find @dropViews, (dropView,i) =>
        eachView = @dropViews[i]

        pendingSaveArr.push(
          view:eachView
          model:
            name:landingView.model.get('name')
            teamId:landingView.model.get('teamId')
            seed:landingView.model.get('seed')
        )

        _.isEqual baseView, dropView

    @chainSaveCallbacks(pendingSaveArr, @checkRemoveFutureWins,[lastLandingView])

  chainSaveCallbacks: (pendingSaveArr,callback,callbackArgs)->
    view = _.first(pendingSaveArr)?.view
    view?.$el.addClass('saving')

    view?.model.save(
      _.first(pendingSaveArr)?.model
    ,
      {
        wait:true
        success: (model,response) =>
          console.log("POST: http://#{window.location.host + model.url()}\nJSON:#{JSON.stringify(model.toJSON())}\n\n")
          view.$el.removeClass('saving')
          if pendingSaveArr.length > 1 #if there are more views
            @chainSaveCallbacks(_.last(pendingSaveArr,pendingSaveArr.length-1),callback,callbackArgs) #save the rest of the views
          else
            callback?.apply(@,callbackArgs)

        error: (model, response) =>
          pendingSaveArr.forEach((viewObj) ->
            viewObj.view.$el.removeClass('saving')
          )
          if response.status is 404
            alert 'Please sign in using twitter.'
          if response.status is 500
            console.log("POST ERROR: http://#{window.location.host + model.url()}\nJSON:#{JSON.stringify(model.toJSON())}\n\n")
      }
    )

  chainDeleteCallbacks: (pendingDeleteArr,callback,callbackArgs)->
    view = _.first(pendingDeleteArr)?.view

    pendingDeleteArr.forEach((userPick) ->
      userPick.view.model.set(userPick.model)
    )

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

  recurPreviousTeamViews: (baseView,actionAttr,actionAttrArgs,previousTeamViewArr) ->
    previousTeamView = null
    previousTeamViewArr = previousTeamViewArr or []

    previousGame = baseView.model.get('previousGame')
    if previousGame
      previousTeamView = @teamViews["#{previousGame.regionId}-#{previousGame.gameId}-#{previousGame.position}"]

    if previousTeamView
      previousTeamView[actionAttr]?.apply(previousTeamView, actionAttrArgs)
      previousTeamViewArr.push(previousTeamView)
      @recurPreviousTeamViews(previousTeamView,actionAttr,actionAttrArgs,previousTeamViewArr)
    else
      previousTeamViewArr

  checkRemoveFutureWins: (baseView) ->
    nextViewArr = @recurNextTeamViews(baseView)
    return if nextViewArr.length is 0

    pendingSaveArr = []
    previousViewArr = @recurPreviousTeamViews(baseView)
    if previousViewArr.length > 0
      prevTeamId = _.first(previousViewArr).model.get('teamId')
      nextViewArr.forEach((view) ->
          if view.model.get('teamId') is prevTeamId
            pendingSaveArr.push
              view:view
              model:
                name:null
                teamId:null
                seed:null
      )

      @chainDeleteCallbacks(pendingSaveArr) if pendingSaveArr.length > 0

  validDropZone: (baseView,landingView) ->
    return false if !landingView

    _.find @dropViews, (dropView) ->
      _.isEqual baseView, dropView