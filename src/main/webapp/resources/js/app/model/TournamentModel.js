(function() {
  define(['lib/backbone', 'base/jsonUri'], function(Backbone, jsonUri) {
    return Backbone.Model.extend({
      urlRoot: "" + jsonUri.root + "/tournament",
      id: "1"
    });
  });
}).call(this);
