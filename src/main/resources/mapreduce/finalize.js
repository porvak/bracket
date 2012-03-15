function (key, value) {
    return {score:value.score, available:(value.maxPointsPossible - value.pointsCounted)};
}