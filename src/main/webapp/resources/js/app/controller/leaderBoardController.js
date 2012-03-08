(function() {

  define(['lib/jquery', 'lib/underscore', 'app/view/LeaderBoardView'], function($, _, LeaderBoardView) {
    return {
      init: function() {
        $('.navbar.leaderboard').click(this.toggle);
        $('.navbar.leaderboard').mousedown(function() {
          return $('.navbar.leaderboard').addClass('selected');
        });
        return $('.navbar.leaderboard').mouseup(function() {
          return $('.navbar.leaderboard').removeClass('selected');
        });
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
