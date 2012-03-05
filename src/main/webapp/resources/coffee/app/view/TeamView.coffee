define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/teamTemplate.html'
], (Backbone, $, handlebars, strTeamTemplate) ->
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
          @trigger('drag',@model)
        stop:(event, ui) =>
          @trigger('drop',@model)
      )

      teamDiv.droppable(
        drop: @drop
        tolerance: 'pointer'
        over: (event, ui) ->
#              $(event.target).addClass "team-droppable"
        out:  (event, ui) ->
#              $(event.target).removeClass "team-droppable"
      )

    events:
      "click .detail": "click"

    click: (e) ->
      console.log(@model.get('name'))

    drop: (event, ui) ->
      $(this).attr('style', 'background-color:red')

    showDropZone: ->
      @$el.addClass "highlight-game-drop"
      console.log("#{@model.get('gameId')}")

    hideDropZone: ->
      @$el.removeClass "highlight-game-drop"
      console.log("#{@model.get('gameId')}")




  )
