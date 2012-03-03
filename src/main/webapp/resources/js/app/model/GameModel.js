(function() {
  define(['lib/backbone', 'base/jsonUri'], function(Backbone, jsonUri) {
    return Backbone.Model.extend({
      url: function() {
        return "/api/pool/" + jsonUri.poolId + "/region/" + (this.get('regionId')) + "/game/" + (this.get('gameId'));
      }
    });
  });
}).call(this);
