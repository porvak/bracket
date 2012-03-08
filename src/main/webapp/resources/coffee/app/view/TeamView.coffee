define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/teamTemplate.html'
], (Backbone, $, handlebars, strTeamTemplate) ->
  Backbone.View.extend

    initialize: (options) ->
      @model.on('change', @render, @)
      @teamHB = handlebars.compile strTeamTemplate
      @render()

    render: ->
      if not @model.get('teamId')
        userPick = @model.get('userPick')
        if userPick and userPick.id   #TODO Change to teamId
          displayTeam = userPick
          displayTeam.teamId = userPick.id  #TODO Change to teamId
        else
          displayTeam = @model.toJSON()
      else
        displayTeam = @model.toJSON()

      if @$el.html()
        @$el.html($(@teamHB(displayTeam)).html())
      else
        @$el = $(@teamHB(displayTeam))

      @$el.addClass 'pickable' if @model.get 'pickable'
      @setupDrag() if @model.get 'teamId'
      @setupDrop()
      @checkWrongPick()

    checkWrongPick: ->
      if @model.get('userPick') and @model.get('teamId') and @model.get('teamId') isnt @model.get('userPick')?.id #TODO change to teamid
        @$el.find('.team').addClass('lineThrough')

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