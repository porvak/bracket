define [
  'lib/jquery'
  'lib/underscore'
  'lib/handlebars'
  'app/model/TournamentModel'
  'app/model/TeamModel'
  'app/view/TeamView'
  'text!html/sectionTemplate.html'
  'text!html/gameTemplate.html'
], ($, _, handlebars, TournamentModel, TeamModel, TeamView, strSectionTemplate, strGameTemplate) ->

  init: ->
    @model = new TournamentModel
    @model.bind('change', @render, @)
    @model.fetch()
    @teamViews = {}
    @dropViews = []
    @sectionHB = handlebars.compile(strSectionTemplate)
    @gameHB = handlebars.compile(strGameTemplate)


  render: ->
    console.log("GET: http://#{window.location.host + @model.url()}\n\n")

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
        ))

        round.games?.forEach (game) =>
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

            @teamViews[team.locator] = teamView

            teamZero = elGame.find('.detail.team-0')
            if teamZero.val()
              teamView.$el.insertAfter(teamZero)
            else
              teamView.$el.insertAfter(elGame.find('.detail.state'))

          elRound.append(elGame)
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
        baseView:endingView
        landingView:startingView
      ])
      @checkBrokenLinks(endingView)



  teamDrop: (baseView,ui) ->
    postError = false
    pendingViews = []

    landingView = @teamViews["#{ui.draggable.data('locator')}"]

    if @validDropZone(baseView,landingView)
      lastLandingView = _.find @dropViews, (dropView,i) =>
        eachView = @dropViews[i]

        eachView.$el.addClass('.saving')
        pendingViews.push(
          baseView:eachView
          landingView:landingView
        )

        _.isEqual baseView, dropView

    @chainSaveCallbacks(pendingViews)
    @checkBrokenLinks(lastLandingView)


  chainSaveCallbacks: (pendingViews)->
    baseView = _.first(pendingViews)?.baseView
    landingView = _.first(pendingViews)?.landingView

    baseView?.model.save(
      {
        name:landingView.model.get('name')
        teamId:landingView.model.get('teamId')
        seed:landingView.model.get('seed')
      }
    ,
      {
        wait:true
        success: (model,response) =>
          console.log("POST: http://#{window.location.host + model.url()}\nJSON:#{JSON.stringify(model.toJSON())}\n\n")
          baseView.$el.removeClass('saving')
          if pendingViews.length > 1 #if there are more views
            @chainSaveCallbacks(_.last(pendingViews,pendingViews.length-1)) #save the rest of the views

        error: (model, response) =>
          pendingViews.forEach((viewObj) ->
            viewObj.baseView.$el.removeClass('saving')
          )
          if response.status is 404
            alert 'Please sign in using twitter.'
      }
    )


  validDropZone: (baseView,landingView) ->
    return false if !landingView

    _.find @dropViews, (dropView) ->
      _.isEqual baseView, dropView


  checkBrokenLinks: (baseView) ->
    nextViewArr = @recurNextTeamViews(baseView)
    if nextViewArr.length is 0
      return

    previousViewArr = @recurPreviousTeamViews(baseView)
    if previousViewArr.length > 0
      @recurNextTeamViews(baseView,'brokenLink',[baseView.model.get('teamId')])


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
