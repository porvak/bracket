define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/scoreTemplate.html'
], (Backbone, $, handlebars, strScoreTemplate) ->
  Backbone.View.extend

    initialize: (options) ->
      @model.on('change', @update, @)
      @scoreHB = handlebars.compile strScoreTemplate
      @render()

    render: ->
      @$el = $(@scoreHB({}))

      if !@model.get('tieBreaker')
        @editOn()

    events:
      "click input": "editOn"
      "blur input": "editOff"

    editOn: (e) ->
      console.log('edit on')

    editOff: (e) ->
      console.log('edit off')
