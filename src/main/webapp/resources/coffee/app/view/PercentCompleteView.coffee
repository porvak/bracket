define [
  'lib/backbone'
  'lib/jquery'
  'lib/handlebars'
  'text!html/percentComplete.html'
], (Backbone, $, handlebars, strPercentComplete) ->
  Backbone.View.extend

    initialize: (options) ->
      @percentHB = handlebars.compile strPercentComplete

    render: (teamViewArr,scoreView) ->
      if @$el.html()
        @$el.html($(@percentHB(percent:@calculate(teamViewArr,scoreView))).html())
      else
        @$el = $(@percentHB(percent:@calculate(teamViewArr,scoreView)))
        $('#bracketNode').append(@$el)

    calculate: (arr,scoreView) ->
      complete = 0
      totalPicks = -1 #account for double team on national championship game

      for own locator, teamView of arr
        if !teamView.model.get('teamId')
          totalPicks++
          if teamView.model.get('userPick')?.teamId
            complete++

      totalPicks++
      if scoreView.model.get('tieBreaker')
        complete++


      "#{Math.round(((complete/totalPicks)*100))}%"