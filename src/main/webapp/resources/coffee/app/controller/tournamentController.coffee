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
            teamView.on('drag',@teamDrag,@)
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


  teamDrag: (baseView) ->
    @dropViews = @recurNextGames(baseView.model,'showDropZone')


  teamDrop: (baseView,ui) ->
    @hideDropZones()
    postError = false
    pendingViews = []

    landingView = @teamViews["#{ui.draggable.data('locator')}"]

    if @validDropZone(baseView,landingView)
      _.find @dropViews, (dropView,i) =>
        eachView = @dropViews[i]

        eachView.$el.addClass('.saving')
        pendingViews.push(
          baseView:eachView
          landingView:landingView
        )

        _.isEqual baseView, dropView

    @chainSaveCallbacks(pendingViews)

#      @checkBrokenLinks(baseView)

  chainSaveCallbacks: (pendingViews)->
    baseView = pendingViews[0].baseView
    landingView = pendingViews[0].landingView

    baseView.model.save
      name:landingView.model.get('name')
      teamId:landingView.model.get('teamId')
      seed:landingView.model.get('seed')
    ,
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



  hideDropZones:() ->
    if @dropViews.length > 0
      @dropViews.forEach((view) ->
          view.hideDropZone()
      )
    else
      @dropViews = @recurNextGames(model,'hideDropZone')


  validDropZone: (baseView,landingView) ->
    if baseView.model.get('regionId') isnt landingView.model.get('regionId') then return false
    if baseView.model.get('roundId') <= landingView.model.get('roundId') then return false
    _.find @dropViews, (dropView) ->
      _.isEqual baseView, dropView


  checkBrokenLinks: (baseView) ->
    #if game exists in previous round
    locator = baseView.model.get('locator')
    region = locator.substr(0,1)
    round = locator.substr(2,1)
    game = locator.substr(4,1)
    position = locator.substr(6,1)

    #TODO - HERE
    @teamViews["#{region}-#{round}-#{iterGame}-#{iterPosition}"]

    #and if next round, delete this game next round on

    @recurNextGames(baseView.model,'reset')



  recurNextGames: (model,actionAttr,dropViews) ->
    nextGame = model.get('nextGame')
    nextGameView = null
    dropViews = dropViews or []

    #Find the next game location
    _.find(@model.get('regions'), (region) =>
      _.find(region.rounds, (round) =>
        _.find(round.games, (game) =>
            if game.gameId is nextGame.gameId and region.regionId is nextGame.regionId
              nextGameView = @teamViews["#{region.regionId}-#{round.roundId}-#{game.gameId}-#{nextGame.position}"]
        )
      )
    )

    nextGameView?[actionAttr]?()
    if nextGameView then dropViews.push(nextGameView)

    if nextGameView and nextGameView.model.get('nextGame')
      @recurNextGames(nextGameView.model,actionAttr,dropViews)
    else dropViews
