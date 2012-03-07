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
            team.locator = "#{region.regionId}-#{round.roundId}-#{game.gameId}-#{team.position}"

            teamView = new TeamView(
              model:new TeamModel(team)
            )
            teamView.on('drag',@findShowDropViews,@)
            teamView.on('dragStop',@hideDropZones,@)
            teamView.on('drop',@teamDrop,@)

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


  hideDropZones:() ->
    if @dropViews.length > 0
      @dropViews.forEach (view) ->
        view.hideDropZone()
    else
      @dropViews = @recurNextGames(model,'hideDropZone')


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
    #if game exists in previous round
    locator = baseView.model.get('locator')
    teamId = baseView.model.get('teamId')
    region = parseInt(locator.substr(0,1),10)
    round = parseInt(locator.substr(2,1),10)
    game = parseInt(locator.substr(4,1),10)
    position = parseInt(locator.substr(6,1),10)

    i=0
    inNextRound=false
    while !inNextRound && i<17
      if @teamViews["#{region}-#{round+1}-#{i}-#{0}"]
        inNextRound = true if teamId == @teamViews["#{region}-#{round+1}-#{i}-#{0}"].model.get('teamId')
        inNextRound = true if teamId == @teamViews["#{region}-#{round+1}-#{i}-#{1}"].model.get('teamId')
      i++

    if inNextRound
      i=0
      inPreviousRound=false
      while !inPreviousRound && i<17
        if @teamViews["#{region}-#{round-1}-#{i}-#{0}"]
          inPreviousRound = true if teamId == @teamViews["#{region}-#{round-1}-#{i}-#{0}"].model.get('teamId')
          inPreviousRound = true if teamId == @teamViews["#{region}-#{round-1}-#{i}-#{1}"].model.get('teamId')
        i++

      if inPreviousRound
        @recurNextTeamViews(baseView,'brokenLink',[teamId])


  recurNextTeamViews: (baseView,actionAttr,actionAttrArgs,nextTeamViewArr) ->
    nextGame = baseView.model.get('nextGame')
    nextTeamView = null
    nextTeamViewArr = nextTeamViewArr or []

    #Find the next game location
    _.find(@model.get('regions'), (region) =>
        _.find(region.rounds, (round) =>
            _.find(round.games, (game) =>
                if game.gameId is nextGame.gameId and region.regionId is nextGame.regionId
                  nextTeamView = @teamViews["#{region.regionId}-#{round.roundId}-#{game.gameId}-#{nextGame.position}"]
            )
        )
    )

    nextTeamView[actionAttr].apply(nextTeamView, actionAttrArgs) if nextTeamView[actionAttr]

    if nextTeamView then nextTeamViewArr.push(nextTeamView)

    if nextTeamView and nextTeamView.model.get('nextGame')
      @recurNextTeamViews(nextTeamView,actionAttr,actionAttrArgs,nextTeamViewArr)
    else nextTeamViewArr

  recurPreviousTeamViews: (baseView,actionAttr,actionAttrArgs,previousTeamViewArr) ->
    previousTeamView = null
    previousTeamViewArr = previousTeamViewArr or []
    gameId = baseView.model.get('gameId')
    teamId = baseView.model.get('teamId')

    #Find the next game location
    _.find(@model.get('regions'), (region) =>
      _.find(region.rounds, (round) =>
        _.find(round.games, (game) =>
          if gameId is game.nextGame?.gameId
            _.find(game.teams, (team) =>
              if team.teamId is teamId
                previousTeamView = @teamViews["#{region.regionId}-#{round.roundId}-#{game.gameId}-#{team.position}"]
            )
        )
      )
    )

    previousTeamView[actionAttr].apply(previousTeamView, actionAttrArgs) if previousTeamView[actionAttr]

    if previousTeamView then previousTeamViewArr.push(previousTeamView)

    if previousTeamView and previousTeamView.model.get('nextGame')
      @recurNextTeamViews(previousTeamView,actionAttr,actionAttrArgs,previousTeamViewArr)
    else previousTeamViewArr