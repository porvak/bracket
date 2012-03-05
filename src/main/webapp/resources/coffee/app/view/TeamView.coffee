define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'app/controller/tournamentController'
  'text!html/teamTemplate.html'
], (Backbone, $, handlebars, tournamentController, strTeamTemplate) ->
  Backbone.View.extend(
    initialize: (options) ->
      @model.on('change', @render, @)
      @teamHB = handlebars.compile(strTeamTemplate)
      @render()

    render: ->
      @$el = $(@teamHB(@model.toJSON()))
      teamDiv = $(@$el.find('.team'))

      teamDiv.draggable(
        helper: 'clone'
        opacity: 0.6
        start:(event, ui) =>
          @trigger('drag',@model,ui)
      )

      teamDiv.droppable(
        tolerance: 'pointer'
        drop: (event,ui) =>
          @trigger('drop',@model,ui)
        over: (event, ui) ->
#              $(event.target).addClass "team-droppable"
        out:  (event, ui) ->
#              $(event.target).removeClass "team-droppable"
      )

    events:
      "click .detail": "click"

    click: (e) ->
      console.log(@model.get('name'))

    showDropZone: ->
      @$el.addClass "highlight-game-drop"

    hideDropZone: ->
      @$el.removeClass "highlight-game-drop"

    isValidDrop: ->
      #dropModel.regionId === @model.regionId
      #dropModel.roundId >= @model.roundId

  )
