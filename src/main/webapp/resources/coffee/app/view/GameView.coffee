define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/gameTemplate.html'
], (Backbone, $, handlebars, strGameTemplate) ->
  Backbone.View.extend(
    initialize: (options) ->
      @model.on('change', @render, @)
      @gameHB = handlebars.compile(strGameTemplate)
      @render()

    render: ->
      @$el.html(@gameHB(@model.toJSON()))
      @$el.find('.team').each(
        (index, team) =>
          $(team).draggable(
            helper: 'clone'
            opacity: 0.6
            start:(event, ui) =>
              @trigger('drag',@model)
            stop:(event, ui) =>
              @trigger('drop',@model)
          )

          $(team).droppable(
            drop: @drop
            tolerance: 'pointer'
            over: (event, ui) ->
#              $(event.target).addClass "team-droppable"
            out:  (event, ui) ->
#              $(event.target).removeClass "team-droppable"
          )
      )

    events:
      "click .game .details .detail": "click"

    click: (e) ->
      team = @getTeam(e)
      console.log(team.name)

    drop: (event, ui) ->
      $(this).attr('style', 'background-color:red')


    getTeam: (e) ->
      seatStr = e?.currentTarget?.getAttribute('position')
      seatNum = parseInt(seatStr)
      @model.get('teams')?[seatNum]

    showDropZone: ->
      @$el.addClass "highlight-game-drop"
      console.log(@model.get('gameId'))

    hideDropZone: ->
      @$el.removeClass "highlight-game-drop"
      console.log(@model.get('gameId'))




  )
