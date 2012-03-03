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
    @gameViewArr = []
    @model.bind('change', @render, @)
    @model.fetch()
    @sectionHB = handlebars.compile(strSectionTemplate)

  render: ->
    elBracket = $(@sectionHB(
      class: "regions"
    ))

    @model.get('regions')?.forEach (region) =>
      elRegion = $(@sectionHB(
        class: "region region-#{region.id}"
      ))

      region.rounds?.forEach (round) =>
        elRound = $(@sectionHB(
          class: "round round-#{round.roundId}"
        ))

        round.games?.forEach (game) =>
          game.regionId = region.id
          gameModel = new GameModel(game)
          gameView = new GameView(model: gameModel)
          elRound.append(gameView.el)

        elRegion.append(elRound)

      elBracket.append(elRegion)

    $('#bracketNode').append(elBracket)
