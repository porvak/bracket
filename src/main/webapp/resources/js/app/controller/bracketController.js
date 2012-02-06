(function() {
  var __bind = function(fn, me){ return function(){ return fn.apply(me, arguments); }; };
  define(['lib/backbone', 'base/jsonUri', 'lib/jquery'], function(Backbone, jsonUri, $) {
    return {
      construct: function() {
        this.contentNode = $("#bracketNode");
        this.collection = new Backbone.Collection;
        return this.collection.url = jsonUri.teams;
      },
      init: function() {
        var _ref, _ref2;
        this.construct();
        if ((_ref = this.contentNode) != null) {
          _ref.html("<h4>Coffeescript's bracket controller says hello.</h4>");
        }
        return (_ref2 = this.collection) != null ? _ref2.fetch({
          url: this.url,
          success: __bind(function() {
            return this.writeBracket();
          }, this),
          error: __bind(function() {
            return this.fetchFail();
          }, this)
        }) : void 0;
      },
      writeBracket: function() {
        return this.collection.each(__bind(function(team, i) {
          var _ref;
          return (_ref = this.contentNode) != null ? _ref.append("<h5>" + (team != null ? team.get("regionName") : void 0) + "</h5>") : void 0;
        }, this));
      },
      fetchFail: function() {
        var _ref;
        return (_ref = window.console) != null ? _ref.log("Fetch failed on " + this.uri) : void 0;
      }
    };
  });
}).call(this);
