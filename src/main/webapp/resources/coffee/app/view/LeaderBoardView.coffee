define [
  'lib/backbone'
  'lib/jquery'
  'lib/underscore'
  'lib/handlebars'
  'base/jsonUri'
  'app/model/LeaderBoardModel'
  'app/view/LeaderBoardEntryView'
  'app/model/LeaderBoardCollection'
  'text!html/leaderBoardTemplate.html'

], (Backbone, $, _, handlebars, jsonUri, LeaderBoardModel, LeaderBoardEntryView, LeaderBoardCollection, strLeaderBoardTemplate) ->
  Backbone.View.extend(

    initialize: (options) ->
      @leaderBoardHB = handlebars.compile(strLeaderBoardTemplate)
      @leaderBoardCollection = LeaderBoardCollection
      @leaderBoardCollection.bind('all', @renderEntries, @)
      @leaderBoardCollection.fetch()

    renderEntries: ->
      @els = @leaderBoardCollection.map ((entry) ->
        leaderBoardEntryView = new LeaderBoardEntryView(model: entry)
        leaderBoardEntryView.render()
        leaderBoardEntryView.el
      )

      leaderBoardObj = $(@leaderBoardHB())
      leaderBoardObj.find('tbody').append(@els)
      @$el.html(leaderBoardObj)
    toggle: ->
      if @$el.dialog('option','autoOpen')
        @$el.dialog({
         autoOpen: false,
         title: 'LeaderBoard'
        })

      if @$el.dialog('isOpen')
        @$el.dialog('close')
      else
        @$el.dialog('open')

  )