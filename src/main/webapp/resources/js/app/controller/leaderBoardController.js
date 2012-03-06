(function() {

  define(['lib/jquery', 'lib/underscore', 'app/view/LeaderBoardView'], function($, _, LeaderBoardView) {
    return {
      init: function() {
        return $('.leaderboard').click(this.toggle);
      },
      toggle: function() {
        if (this.leaderBoard) {
          return this.leaderBoard.toggle();
        } else {
          this.leaderBoard = new LeaderBoardView();
          return this.leaderBoard.toggle();
        }
      }
    };
  });

}).call(this);
