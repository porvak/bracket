(function() {

  define(['lib/backbone', 'base/jsonUri'], function(Backbone, jsonUri) {
    return Backbone.Model.extend({
      urlRoot: "" + jsonUri.root + "/api/pub/tournament",
      id: "4f41ce03d17060d0d8dbd4d6"
    });
  });

}).call(this);
