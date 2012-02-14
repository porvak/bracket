define [
  'lib/backbone'
  'lib/jquery'
  'app/view/RegionView'
], (Backbone, $, RegionView) ->
  Backbone.View.extend(
    initialize: (options) ->
      @model.bind('change',@render,@)

    render: ->
      regionCollection = new Backbone.Collection(@model.get('regions'))
      regionCollection.each (region) ->
        regionView = new RegionView(
          model:new Backbone.Model(region)
        )
        regionView.render()

  );