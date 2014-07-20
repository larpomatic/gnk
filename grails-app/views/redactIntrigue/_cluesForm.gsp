%{--<div class="tabbable tabs-left clueScreen">--}%
    %{--<ul class="nav nav-tabs leftUl">--}%
        %{--<li class="active leftMenuList">--}%
            %{--<a href="#newClue" data-toggle="tab" class="addClue">--}%
                %{--<g:message code="redactintrigue.clue.addClue" default="New clue"/>--}%
            %{--</a>--}%
        %{--</li>--}%

    %{--</ul>--}%

    %{--<div class="tab-content">--}%
        %{--<div class="tab-pane active" id="newClue">--}%
            %{--<form name="newClueForm" data-url="">--}%
                %{--<div style="margin:auto">--}%
                %{--<div class="row formRow">--}%
                    %{--<div class="span1">--}%
                        %{--<label for="clueTitle">--}%
                            %{--<g:message code="redactintrigue.clue.clueTitle" default="Title"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<g:textField name="clueTitle" id="clueTitle" value="" required=""/>--}%
                    %{--</div>--}%
                %{--</div>--}%
                %{--<div class="row formRow">--}%
                    %{--<div class="span1">--}%
                        %{--<label for="clueVersion">--}%
                            %{--<g:message code="redactintrigue.clue.clueVersion" default="Version"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<g:textField name="clueVersion" id="clueVersion" value="" required=""/>--}%
                    %{--</div>--}%

                    %{--<div class="span1">--}%
                        %{--<label for="cluePossessor">--}%
                            %{--<g:message code="redactintrigue.clue.clueRolePossessor" default="Possessor"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<g:select name="cluePossessor" id="cluePossessor" from="${['Role1', 'Role2', 'Role3']}"--}%
                                  %{--keys="${['Role1', 'Role2', 'Role3']}" required=""/>--}%
                    %{--</div>--}%
                %{--</div>--}%
                %{--<div class="row formRow">--}%
                    %{--<div class="span1">--}%
                        %{--<label for="clueRoleFrom">--}%
                            %{--<g:message code="redactintrigue.clue.clueRoleFrom" default="From role"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<g:select name="clueRoleFrom" id="clueRoleFrom" from="${['Role1', 'Role2', 'Role3']}"--}%
                                  %{--keys="${['Role1', 'Role2', 'Role3']}" required=""/>--}%
                    %{--</div>--}%

                    %{--<div class="span1">--}%
                        %{--<label for="clueRoleTo">--}%
                            %{--<g:message code="redactintrigue.clue.clueRoleTo" default="To role"/>--}%
                        %{--</label>--}%
                    %{--</div>--}%

                    %{--<div class="span4">--}%
                        %{--<g:select name="clueRoleTo" id="clueRoleTo" from="${['Role1', 'Role2', 'Role3']}"--}%
                                  %{--keys="${['Role1', 'Role2', 'Role3']}" required=""/>--}%
                    %{--</div>--}%
                %{--</div>--}%

                %{--<div class="row formRow text-center">--}%
                    %{--<label for="clueDescription">--}%
                        %{--<g:message code="redactintrigue.clue.clueDescription" default="Description"/>--}%
                    %{--</label>--}%
                %{--</div>--}%
                %{--<g:textArea name="clueDescription" id="clueDescription" value="" rows="5" cols="100"/>--}%
                %{--</div>--}%
                %{--<input type="button" name="Insert" value="Insert" class="btn btn-primary insertClue"/>--}%
            %{--</form>--}%
        %{--</div>--}%
    %{--</div>--}%
%{--</div>--}%