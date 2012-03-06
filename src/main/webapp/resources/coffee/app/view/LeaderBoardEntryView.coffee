define [
  'lib/backbone'
  'lib/jquery'
  'text!html/leaderBoardEntryTemplate.html'
], (Backbone, $, strLeaderBoardEntryTemplate) ->
  Backbone.View.extend(
    tagName: 'tr'
    events:
        mouseenter: "showHover"
        mouseleave: "hideHover"

    initialize: (options) ->
      @model = this.options.model
      @entryHB = Handlebars.compile(strLeaderBoardEntryTemplate)

    render: ->
      @$el.html(@entryHB(@model.toJSON()))
      @

    showHover: (event) ->
      $(@.el).addClass("highlight")

    hideHover: (event) ->
      $(@.el).removeClass("highlight")
  )