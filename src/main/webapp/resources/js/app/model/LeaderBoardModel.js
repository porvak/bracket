(function() {

  define(['lib/backbone', 'base/jsonUri'], function(Backbone, jsonUri) {
    return Backbone.Model.extend({
      urlRoot: "" + jsonUri.root + "/api/pub/leaderboard/pool/",
      id: "4f3c8297a0eea26b78d77538"
    });
  });

}).call(this);
