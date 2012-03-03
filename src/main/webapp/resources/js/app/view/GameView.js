(function() {
  define(['lib/backbone', 'lib/jquery', 'lib/handlebars', 'text!html/gameTemplate.html'], function(Backbone, $, handlebars, strGameTemplate) {
    return Backbone.View.extend({
      initialize: function(options) {
        this.model.on('change', this.render, this);
        this.gameHB = handlebars.compile(strGameTemplate);
        return this.render();
      },
      render: function() {
        return this.$el.html(this.gameHB(this.model.toJSON()));
      },
      events: {
        "click .game .details .detail": "click"
      },
      click: function(e) {
        var team;
        team = this.getTeam(e);
        return console.log(team.name);
      },
      getTeam: function(e) {
        var seatNum, seatStr, _ref, _ref2;
        seatStr = e != null ? (_ref = e.currentTarget) != null ? _ref.getAttribute('position') : void 0 : void 0;
        seatNum = parseInt(seatStr);
        return (_ref2 = this.model.get('teams')) != null ? _ref2[seatNum] : void 0;
      }
    });
  });
}).call(this);
