define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'app/controller/tournamentController'
  'text!html/teamTemplate.html'
], (Backbone, $, handlebars, tournamentController, strTeamTemplate) ->
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

    triggerAdvance: (e) ->
      @trigger('advance', @)

    showDropZone: ->
      @$el.addClass "highlight-team-drop"

    hideDropZone: ->
      @$el.removeClass "highlight-team-drop"

    reset: (teamId) ->
      @model.set
        name:null
        teamId:null
        seed:null

    brokenLink: (teamId) ->
      if teamId is @model.get('teamId')
        @reset()
