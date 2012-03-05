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


  teamDrag: (model,ui) ->
    @dropViews = @recurNextGames(model,'showDropZone')


  teamDrop: (model,ui) ->
    if @dropViews.length > 0
      @dropViews.forEach((view) ->
          view.hideDropZone()
      )
    else
      @dropViews = @recurNextGames(model,'hideDropZone')

    if not model.get('teamId') #then its an empty team
      landingModel = @teamViews["#{ui.draggable.data('id')}"]?.model

      console.log("Team #{landingModel.get('name')} has landed on game id #{model.get('gameId')}.")
#        @model.set('teamId',landingTeamView.model.get('name'))
#        @model.save()




  #Calls an action on all of the next valid games recursively
  recurNextGames: (model,actionAttr,dropViews) ->
    nextGame = model.get('nextGame')
    nextGameView = undefined
    dropViews = [] unless dropViews

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
    if nextGameView then dropViews.push(nextGameView)

    if nextGameView and nextGameView.model.get('nextGame') then @recurNextGames(nextGameView.model,actionAttr,dropViews) else dropViews


