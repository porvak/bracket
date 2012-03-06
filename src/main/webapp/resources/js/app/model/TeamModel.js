(function() {

  define(['lib/backbone', 'base/jsonUri'], function(Backbone, jsonUri) {
    return Backbone.Model.extend({
      url: function() {
        return "" + jsonUri.root + "/api/pool/" + jsonUri.poolId + "/user/pick";
      }
    });
  });

}).call(this);
