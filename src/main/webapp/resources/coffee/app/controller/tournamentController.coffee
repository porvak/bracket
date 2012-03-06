define [
  'lib/jquery'
  'lib/handlebars'
  'app/model/TournamentModel'
  'app/model/TeamModel'
  'app/view/TeamView'
  'text!html/sectionTemplate.html'
  'text!html/gameTemplate.html'
], ($, handlebars, TournamentModel, TeamModel, TeamView, strSectionTemplate, strGameTemplate) ->
  init: ->
    @model = new TournamentModel
    @model.bind('change', @render, @)
    @model.fetch()
    @teamViews = {}
    @emptyTeamViews = {}
    @sectionHB = handlebars.compile(strSectionTemplate)
    @gameHB = handlebars.compile(strGameTemplate)
    @regionHeaderHB = handlebars.compile("<h2>{{regionName}}</h2>");

  render: ->
    elBracket = $(@sectionHB(
      class: "regions"
    ))

    @model.get('regions')?.forEach (region) =>
      elRegion = $(@sectionHB(
        class: "region region-#{region.regionId}"
      ))

      elRegionHeader = $(@regionHeaderHB(
        regionName: region.name
      ))

      elRegion.append(elRegionHeader)

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

            teamView = new TeamView(model:new TeamModel(team))
            teamView.on('drag',@showDropZones,@)
            teamView.on('drop',@hideDropZones,@)

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


  showDropZones: (model) ->
    @recurNextGames(model,'showDropZone')


  hideDropZones: (model) ->
    @recurNextGames(model,'hideDropZone')


  recurNextGames: (model,actionAttr) ->
    nextGame = model.get('nextGame')
    nextGameView = undefined

    #Find the next game location
    region = _.find(@model.get('regions'), (region) =>
        round = _.find(region.rounds, (round) =>
            game = _.find(round.games, (game) =>
                if game.gameId is nextGame.gameId and region.regionId is nextGame.regionId
                  nextGameView = @emptyTeamViews["#{region.regionId}-#{round.roundId}-#{game.gameId}-#{nextGame.position}"]
            )?
        )?
    )

    nextGameView?[actionAttr]?()

    if nextGameView and nextGameView.model.get('nextGame') then @recurNextGames(nextGameView.model,actionAttr) else null


