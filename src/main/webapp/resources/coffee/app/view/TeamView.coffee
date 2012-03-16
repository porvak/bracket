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
        if userPick and userPick.teamId
          displayTeam = userPick
          displayTeam.locator = @model.get('locator')
          displayTeam.position = @model.get('position')
          displayTeam.teamId = userPick.teamId
        else
          displayTeam = @model.toJSON()
      else
        displayTeam = @model.toJSON()

      if @$el.html()
        @$el.html($(@teamHB(displayTeam)).html())
      else
        @$el = $(@teamHB(displayTeam))

      if @model.get('pickStatus') is 'OPEN' and (@model.get('teamId') or @model.get('userPick')?.teamId)
        @$el.addClass 'pickable'
      else
        @$el.removeClass 'pickable'
      @setupDrag() if (@model.get('teamId') or @model.get('userPick')?.teamId)
      @setupDrop()
      @checkWrongPick()

    checkWrongPick: ->
      if @model.get('userPick') and @model.get('teamId') and @model.get('teamId') isnt @model.get('userPick')?.teamId
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