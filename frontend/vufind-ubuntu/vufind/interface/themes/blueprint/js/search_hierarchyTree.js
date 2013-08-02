$(document).ready(function() {
    $(".hierarchyTreeLink a").click(function() {
        var hierarchyID = $(this).parent().find(".hiddenHierarchyId")[0].value;
        var id = this.id.substr('hierarchyTree'.length);
        var $dialog = getLightbox('Record', 'HierarchyTree', id, null, this.title, '', '', '', {hierarchy: hierarchyID});
        return false;
    });
});
