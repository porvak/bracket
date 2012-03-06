define [
  'lib/jquery'
  'lib/underscore'
  'app/view/LeaderBoardView'
], ($,_,LeaderBoardView) ->
  init: ->
    $('.leaderboard').click(@toggle)


  toggle: ->
    if @leaderBoard
      @leaderBoard.toggle()
    else
      @leaderBoard =  new  LeaderBoardView()
      @leaderBoard.toggle()