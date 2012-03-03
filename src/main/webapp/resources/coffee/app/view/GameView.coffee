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

    events:
      "click .game .details .detail": "click"

    click: (e) ->
      team = @getTeam(e)
      console.log(team.name)

    getTeam: (e) ->
      seatStr = e?.currentTarget?.getAttribute('position')
      seatNum = parseInt(seatStr)
      @model.get('teams')?[seatNum]

  )
