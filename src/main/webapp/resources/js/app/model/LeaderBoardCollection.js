(function() {

  define(['lib/backbone', 'base/jsonUri', 'lib/jquery', 'app/model/LeaderBoardModel'], function(Backbone, jsonUri, $, LeaderBoardModel) {
    return new (Backbone.Collection.extend({
      url: "" + jsonUri.root + "/api/leaderboard/pool/4f3c8297a0eea26b78d77538",
      model: LeaderBoardModel
    }));
  });

}).call(this);
