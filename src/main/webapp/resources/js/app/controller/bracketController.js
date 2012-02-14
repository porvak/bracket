(function() {
  define(['base/jsonUri', 'lib/jquery', 'app/model/TournamentModel', 'app/view/TournamentView'], function(jsonUri, $, TournamentModel, TournamentView) {
    return {
      construct: function() {
        this.contentNode = $("#bracketNode");
        this.model = new TournamentModel;
        this.view = new TournamentView({
          model: this.model
        });
        return this.model.fetch();
      },
      init: function() {
        return this.construct();
      }
    };
  });
}).call(this);
