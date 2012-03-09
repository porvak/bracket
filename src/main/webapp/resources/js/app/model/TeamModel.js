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
            console.log("DELETE: http://" + (window.location.host + ("" + jsonUri.root + "/api/pool/" + jsonUri.poolId + "/region/" + (this.get('regionId')) + "/game/" + (this.get('gameId')) + "/userpick/" + (this.get('position')) + "?")) + "\n\n");
            this.set({
              userPick: null,
              previousGame: null
            });
            return typeof cfg.success === "function" ? cfg.success() : void 0;
          },
          error: function(model, response) {
            console.log("DELETE ERROR: http://" + (window.location.host + ("" + jsonUri.root + "/api/pool/" + jsonUri.poolId + "/region/" + (_this.get('regionId')) + "/game/" + (_this.get('gameId')) + "/userpick/" + (_this.get('position')) + "?")) + "\n\n");
            return typeof cfg.error === "function" ? cfg.error() : void 0;
          }
        });
      }
    });
  });

}).call(this);
