(function() {

  define(['lib/backbone', 'base/jsonUri'], function(Backbone, jsonUri) {
    return Backbone.Model.extend({
      url: function() {
        return "" + jsonUri.root + "/api/pool/" + jsonUri.poolId + "/user/pick";
      },
      deletePick: function(cfg) {
        var _this = this;
        return $.ajax({
          url: "" + jsonUri.root + "/api/pool/" + jsonUri.poolId + "/region/" + (this.get('regionId')) + "/game/" + (this.get('gameId')) + "/userpick/" + (this.get('position')) + "?",
          type: "DELETE",
          success: function(model, response) {
            return typeof cfg.success === "function" ? cfg.success() : void 0;
          },
          error: function(model, response) {
            return typeof cfg.error === "function" ? cfg.error() : void 0;
          }
        });
      }
    });
  });

}).call(this);
