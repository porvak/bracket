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

      @$el.find('table').dataTable(
        "bJQueryUI": true,
        "sScrollY": "260px",
        "bPaginate": false,
        "aaSorting": [[0,'asc'], [9, 'desc'], [1,'asc']],
        "aoColumns": [
            { "sType": "numeric" },
            null,
            { "sType": "numeric" },
            { "sType": "numeric" },
            { "sType": "numeric" },
            { "sType": "numeric" },
            { "sType": "numeric" },
            { "sType": "numeric" },
            { "sType": "numeric" },
            { "sType": "numeric" }
        ]
      )


    toggle: ->
      if @$el.dialog('option','autoOpen')
        @$el.dialog({
          autoOpen: false,
          title: 'LeaderBoard'
          height: 400
          width: 600
          position: ['center',50]
          modal:true
          resizable:false
        })

      if @$el.dialog('isOpen')
        @$el.dialog('close')
      else
        @$el.dialog('open')

  )