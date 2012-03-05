define [
  'lib/jquery'
  'lib/handlebars'
  'app/model/TournamentModel'
  'app/model/GameModel'
  'app/view/GameView'
  'text!html/sectionTemplate.html'
], ($, handlebars, TournamentModel, GameModel, GameView, strSectionTemplate) ->
  init: ->
    @model = new TournamentModel
    @model.bind('change', @render, @)
    @model.fetch()
    @gameViews = {}
    @sectionHB = handlebars.compile(strSectionTemplate)

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
          game.regionId = region.regionId
          game.roundId = round.roundId

          gameModel = new GameModel(game)
          gameView = new GameView(model: gameModel)
          gameView.on('drag',@showDropZones,@)
          gameView.on('drop',@hideDropZones,@)

          @gameViews["#{region.regionId}-#{round.roundId}-#{game.gameId}"] = gameView

          elRound.append(gameView.el)

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

    region = _.find(@model.get('regions'), (region) =>
        round = _.find(region.rounds, (round) =>
            game = _.find(round.games, (game) =>
                if game.gameId is nextGame.gameId and region.regionId is nextGame.regionId
                  nextGameView = @gameViews["#{region.regionId}-#{round.roundId}-#{game.gameId}"]
            )?
        )?
    )

    nextGameView?[actionAttr]?()

    if nextGameView and nextGameView.model.get('nextGame') then @recurNextGames(nextGameView.model,actionAttr) else null


