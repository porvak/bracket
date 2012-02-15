(function() {
  define(['base/jsonUri', 'lib/jquery', 'app/model/TournamentModel', 'app/view/TournamentView'], function(jsonUri, $, TournamentModel, TournamentView) {
    return {
      construct: function() {
        this.model = new TournamentModel;
        return this.view = new TournamentView({
          model: this.model
        });
      },
      init: function() {
        this.construct();
        return this.model.fetch();
      }
    };
  });
}).call(this);
