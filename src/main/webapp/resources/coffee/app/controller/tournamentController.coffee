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
    @emptyTeamViews = {}
    @dropViews = []
    @sectionHB = handlebars.compile(strSectionTemplate)
    @gameHB = handlebars.compile(strGameTemplate)


  render: ->
    elBracket = $(@sectionHB(
      class: "regions"
    ))

    @model.get('regions')?.forEach (region) =>
      elRegion = $(@sectionHB(
        class: "region region-#{region.regionId}"
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

            teamView = new TeamView(
              model:new TeamModel(team)
            )
            teamView.on('drag',@teamDrag,@)
            teamView.on('drop',@teamDrop,@)

            @teamViews["#{team.teamId}"] = teamView
            @emptyTeamViews["#{region.regionId}-#{round.roundId}-#{game.gameId}-#{team.position}"] = teamView unless team.teamId and team.name

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
    if @dropViews.length > 0
      @dropViews.forEach((view) ->
          view.hideDropZone()
      )
    else
      @dropViews = @recurNextGames(model,'hideDropZone')

    landingView = @teamViews["#{ui.draggable.data('id')}"]

    if @validDropZone(baseView,landingView)
      baseView.model.set(
        name:landingView.model.get('name')
        teamId:landingView.model.get('teamId')
        seed:landingView.model.get('seed')
      )
      baseView.model.save()


  validDropZone: (baseView,landingView) ->
    if baseView.model.get('teamId') then return false
    if baseView.model.get('regionId') isnt landingView.model.get('regionId') then return false
    if baseView.model.get('roundId') <= landingView.model.get('roundId') then return false
    _.find @dropViews, (dropView) ->
      _.isEqual baseView, dropView


  recurNextGames: (model,actionAttr,dropViews) ->
    nextGame = model.get('nextGame')
    nextGameView = null
    dropViews = dropViews or []

    #Find the next game location
    _.find(@model.get('regions'), (region) =>
      _.find(region.rounds, (round) =>
        _.find(round.games, (game) =>
            if game.gameId is nextGame.gameId and region.regionId is nextGame.regionId
              nextGameView = @emptyTeamViews["#{region.regionId}-#{round.roundId}-#{game.gameId}-#{nextGame.position}"]
        )?
      )?
    )

    nextGameView?[actionAttr]?()
    if nextGameView then dropViews.push(nextGameView)

    if nextGameView and nextGameView.model.get('nextGame')
      @recurNextGames(nextGameView.model,actionAttr,dropViews)
    else dropViews
