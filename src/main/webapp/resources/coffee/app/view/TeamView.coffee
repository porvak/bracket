define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/teamTemplate.html'
], (Backbone, $, handlebars, strTeamTemplate) ->
  Backbone.View.extend

    initialize: (options) ->
      @model.on('change', @update, @)
      @teamHB = handlebars.compile strTeamTemplate
      @render()

    render: ->
      @$el = $(@teamHB(@model.toJSON()))
      @$el.addClass 'pickable' if @model.get 'pickable'

      @setupDrag() if @model.get 'teamId'
      @setupDrop()

    update: ->
      @$el.html($(@teamHB(@model.toJSON())).html())
      @setupDrag() if @model.get 'teamId'

    setupDrag: ->
      @$el.draggable
        helper: 'clone'
        opacity: 0.6
        zIndex:2000
        start: (event, ui) =>
          @trigger('drag', @, ui)
        stop: (event, ui) =>
          @trigger('dragStop', @, ui)

    setupDrop: ->
      @$el.droppable
        tolerance: 'pointer'
        drop: (event, ui) =>
          @trigger('drop', @, ui)

    events:
      "click .pick_icon": "triggerAdvance"
      "mouseenter .team": "showDelete"
      "mouseleave .team": "hideDelete"
      "click .team-delete": "triggerDelete"

    triggerAdvance: (e) ->
      @trigger('advance', @)

    triggerDelete: (e) ->
      @trigger('remove', @)

    showDropZone: ->
      @$el.addClass "highlight-team-drop"

    hideDropZone: ->
      @$el.removeClass "highlight-team-drop"

    showDelete: ->
      if @model.get('roundId') isnt 1 or @model.get('regionId') is 5
        @$el.find('.team-delete').removeClass "hidden"

    hideDelete: ->
      if @model.get('roundId') isnt 1 or @model.get('regionId') is 5
        @$el.find('.team-delete').addClass "hidden"