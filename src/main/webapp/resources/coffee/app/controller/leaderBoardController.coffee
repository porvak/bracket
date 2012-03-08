define [
  'lib/jquery'
  'lib/underscore'
  'app/view/LeaderBoardView'
], ($,_,LeaderBoardView) ->
  init: ->
    $('.navbar.leaderboard').click(@toggle)
    $('.navbar.leaderboard').mousedown( ->
        $('.navbar.leaderboard').addClass('selected')
    )
    $('.navbar.leaderboard').mouseup( ->
        $('.navbar.leaderboard').removeClass('selected')
    )



  toggle: ->
    if @leaderBoard
      @leaderBoard.toggle()
    else
      @leaderBoard =  new  LeaderBoardView()
      @leaderBoard.toggle()

