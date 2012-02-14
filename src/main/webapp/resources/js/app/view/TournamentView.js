(function() {
  define(['lib/backbone', 'lib/jquery', 'app/view/RegionView'], function(Backbone, $, RegionView) {
    return Backbone.View.extend({
      initialize: function(options) {
        return this.model.bind('change', this.render, this);
      },
      render: function() {
        var regionCollection;
        regionCollection = new Backbone.Collection(this.model.get('regions'));
        return regionCollection.each(function(region) {
          var regionView;
          regionView = new RegionView({
            model: new Backbone.Model(region)
          });
          return regionView.render();
        });
      }
    });
  });
}).call(this);
