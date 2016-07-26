<div class="row-fluid">
    <div class="span4"><legend>Dates</legend></div>
    <div class="span1"><span class="badge badge-important" id="datesPercentage">0 %</span></div>
    <div class="span2"><a id="runSubDateButton" class="btn btn-info"><i class="icon-play icon-white"></i> LANCER</a></div>
    <div class="span1" id="datesLoader" style="display: none; float : right;"><g:img dir="images/substitution" file="loader.gif" width="30" height="30"/></div>
</div>

<div id="subDatesAlertContainer">
</div>

<g:form method="post" controller="substitution">

    <g:hiddenField name="gnId" value="${gnId}"/>
    <g:each in="${sexe}" var="a">
        <g:hiddenField id="a" name="sexe" value="NO"/>
    </g:each>
    <td><label for="t0DateHour"><g:message
            code="selectintrigue.step0.t0Date" default="Actual GN Date"/></label></td>
    <td>
        <g:javascript src="selectIntrigue/dhtmlxcalendar.js"/>
        <link rel="stylesheet" href="${resource(dir: 'css', file: 'dhtmlxcalendar.css')}" type="text/css">
        <div class="input-append">
            <input readonly type="text" id="t0DateHour" name="t0DateHour" placeholder="jj/mm/aaaa hh:mm"
                   required="required" pattern="\d{1,2}/\d{1,2}/\d{4} \d{2}:\d{2}"
                   value="${formatDate(format: 'dd/MM/yyyy HH:mm', date: gnInstance?.t0Date)}"/>
            <script>

                var myCalendar = new dhtmlXCalendarObject(["t0DateHour"]);
                myCalendar.setDateFormat("%d/%m/%Y %H:%i");


            </script>
            <g:if test="${formatDate(format: 'G', date: gnInstance?.t0Date) == 'BC'}">
                <input type="hidden" name="t0DateHourUnity" value="-"/>
                <span class="add-on btn">
                    <i>- JC</i>
                </span>
            </g:if>
            <g:else>
                <input type="hidden" name="t0DateHourUnity" value="+"/>
                <span class="add-on btn">
                    <i>+ JC</i>
                </span>
            </g:else>
        </div>
    </td>

    <td><label for="gnDuration"><g:message
            code="selectintrigue.step0.gnDuration" default="GN duration"/></label></td>
    <td><div class="input-append">
        <input class="span2" name="gnDuration" id="gnDuration"
               type="number" value="${gnInstance?.duration}" required="required" pattern="\d*"
               style="margin-right: 0px;" min="1"/><span class="add-on">h</span>
    </div></td>

    <g:actionSubmit class="btn btn-primary" action="saveOrUpdateDates"
                    value="${message(code: 'default.button.update.label', default: 'Update')}"/>
</g:form>



<g:form method="post" controller="Gantt">
    <g:hiddenField name="gnId" value="${gnId}"/>
    <g:each in="${sexe}" var="a">
        <g:hiddenField id="a" name="sexe" value="NO"/>
    </g:each>
    <g:if test="${gnInfo.duration > 0}">

    <g:javascript src="substitution/dhtmlxgantt.js"></g:javascript>
    <link href="${resource(dir: 'css', file: 'dhtmlxgantt.css')}" rel="stylesheet"/>
    <div id="gantt_here" name="gant_here" style='width:1100px; height:400px;'></div>
    <script type="text/javascript">

        gantt.config.start_date =  "${gnInfo.t0Date.format('yyyy.MM.dd HH:mm')}";

        var dated = new Date("${gnInfo.t0Date.format('yyyy.MM.dd HH:mm')}");
        dated.setTime(dated.getTime() + ${gnInfo.duration}*60*60*1000);


    <g:if test="${gnInfo.GanttData =! null && gnInfo.GanttData != ""}">
        var tasks_XML = "${gnInfo.GanttData}";
        tasks = gantt.parse(tasks_XML, "xml");
     </g:if>
    <g:else>
        tasks = {
            data:[
                {id:1, text:"GN",start_date:"${gnInfo.t0Date.format('dd.MM.YYYY HH:mm')}", duration:${gnInfo.duration},
                    progress: 0.6, open: true, color:"blue", users:["Non Bloquante, Publique"], maboule: "oiu"},
                /*{id:2, text:"Etape 1",   start_date:"10-10-2010 14:00", duration:2,
                    progress: 1,   open: true, parent:1},
                {id:3, text:"Etape 2",   start_date:"10-10-2010 15:00", duration:3,
                    progress: 0.5, open: true, parent:1},
                {id:4, text:"Etape 2.1", start_date:"10-10-2010 16:00", duration:4,
                    progress: 1,   open: true, parent:3},
                {id:5, text:"Etape 2.2", start_date:"10-10-2010 17:00", duration:5,
                    progress: 0.8, open: true, parent:3},
                {id:6, text:"Etape 2.3", start_date:"10-10-2010 18:00", duration:6,
                    progress: 0.2, open: true, parent:3}*/
            ],
            links:[
                {id:1, source:1, target:2, type:"1"},
                {id:2, source:1, target:3, type:"1"},
                {id:3, source:3, target:4, type:"1"},
                {id:4, source:4, target:5, type:"0"},
                {id:5, source:5, target:6, type:"0"}
            ]
        };
    </g:else>

        gantt.config.xml_date="%d-%m-%Y %H:%i";
        gantt.config.fit_tasks = true;
        gantt.config.min_duration = 1000*60;
        gantt.config.duration_unit = "hour";//an hour
        gantt.config.duration_step = 1;
        gantt.config.time_step = 1;
        gantt.config.scale_height = 54;
        gantt.config.scale_unit = "day";
        gantt.config.date_scale = "%d-%m-%Y";
        gantt.config.subscales = [
            {unit:"hour", step:1, date:"%H:%i"},

        ];

        gantt.form_blocks["my_editor"] = {
        render:function(sns) {
            return "<div class='dhx_cal_ltext' style='height:60px;'>Nom&nbsp;"
            +"<input type='text'><br/>Nature&nbsp;<select><option value='Bloquante, Publique'>Bloquante, Publique</option><option value='Non bloquante, Publique'>Non bloquante, Publique</option><option value='Bloquante, Non Publique'>Bloquante, Non Publique</option><option value='Non Bloquante, Non Publique'>Non Bloquante, Non Publique</option></select>"
            + "<br/></div>";
        },
        set_value:function(node, value, task,section) {
            node.childNodes[1].value = value || "";
            node.childNodes[4].value = task.color || "";

        },
        get_value:function(node, task,section) {
            if (node.childNodes[4].value == "Bloquante, Publique" || node.childNodes[4].value == "Bloquante, Non Publique")
               task.color = "red";
            else
               task.color = "blue";
            task.users = node.childNodes[4].value
            return node.childNodes[1].value;
        },
        focus:function(node) {
            var a = node.childNodes[1];
            a.select();
            a.focus();
        }
        };

        gantt.config.lightbox.sections = [
            {name:"details", height:158 , map_to:"text", type:"my_editor",focus:true},
            {name:"description", height:158 , map_to:"text", type:"textarea",focus:true},
            {name:"time", height:72, type:"duration", map_to:"auto", time_format:["%d","%m", "%Y", "%H:%i"]}
        ];

        gantt.templates.task_text=function(start,end,task){
        return task.text+ ", "+task.users;
        };


         // move all the children when you are dragging the parent
        gantt.attachEvent("onTaskDrag", function(id, mode, task, original){
            var modes = gantt.config.drag_mode;
            if(mode == modes.move){
                var diff = task.start_date - original.start_date;
                var stop = false;
                gantt.eachTask(function(child){
                  if ((new Date(+child.start_date + diff) >= task.start_date) || task.start_date == original.start_date){
                    child.start_date = new Date(+child.start_date + diff);
                    child.end_date = new Date(+child.end_date + diff);
                    //gantt.roundTaskDates(child);
                    gantt.refreshTask(child.id, true);
                  }

                },id );
            }
            return true;
        });

        //rounds positions of the child items to scale
        gantt.attachEvent("onAfterTaskDrag", function(id, mode, e){
            var modes = gantt.config.drag_mode;
            if(mode == modes.move ){
                gantt.eachTask(function(child){
                    gantt.roundTaskDates(child);
                    gantt.refreshTask(child.id, true);
                },id );
            }
        });


        // prevent tasks to be dragt before their parent or behind bloquante
        gantt.attachEvent("onTaskDrag", function(id, mode, task, original){
            var modes = gantt.config.drag_mode;
            var taskObj = gantt.getTask(id);//-> {id:"t1", text:"Task #5", parent:"pr_2", ...}
            if (taskObj.parent != null && taskObj.parent > 0) {
                var taskParent = gantt.getTask(taskObj.parent);

                if ((mode == modes.move || mode == modes.resize) && taskParent != null ) {

                    var diff = original.duration * (1000 * 60 * 60);
                    var aaa = taskParent.end_date;

                    if (+task.start_date < +aaa ) {
                        task.start_date = new Date(aaa);
                        if (mode == modes.move)
                            task.end_date = new Date(+aaa.end_date + task.duration);
                    }
                }
            }
            return true;
        });


        // prevent tasks to be created before their parents
        gantt.attachEvent("onAfterTaskAdd", function(id, mode, task, original){


            var taskObj = gantt.getTask(id);//-> {id:"t1", text:"Task #5", parent:"pr_2", ...}

            if (taskObj.parent != null && taskObj.parent > 0) {

                var taskParent = gantt.getTask(taskObj.parent);
                if (taskParent != null ) {

                    if (taskObj.start_date <= taskParent.end_date) {
                        taskObj.start_date = new Date(taskParent.end_date);
                        taskObj.end_date =  gantt.calculateEndDate(taskParent.end_date, taskObj.duration, "hour");
                    }
                }
            }
            return true;
        });

        // one parent, one children
        gantt.attachEvent("onAfterTaskAdd", function(id, mode, task, original){

            var taskObj = gantt.getTask(id);//-> {id:"t1", text:"Task #5", parent:"pr_2", ...}
            if (taskObj.parent != null && taskObj.parent > 0) {

                var taskParent = gantt.getTask(taskObj.parent);

              if (gantt.hasChild(taskParent.id)) {
                    var tabchild = gantt.getChildren(taskParent.id);

                if (tabchild.length > 1 ) {
                    var taskChild = gantt.getTask(tabchild[0]);


                    if (!gantt.hasChild(taskChild.id) && taskChild != null) {
                        taskObj.parent = taskChild.parent;
                        taskChild.parent = taskObj.id;
                        if (taskObj.end_date > taskChild.start_date) {
                          taskChild.start_date = new Date(taskObj.end_date);
                          taskChild.end_date =  gantt.calculateEndDate(taskObj.end_date, taskChild.duration, "hour");
                        }
                        gantt.updateTask(taskObj.id);
                        gantt.updateTask(taskChild.id);
                        gantt.render();

                    } else {
                       diff = taskObj.duration;
                      taskObj.parent = taskChild.parent;
                      taskChild.parent = taskObj.id;
                      if (taskObj.end_date > taskChild.start_date) {
                          taskChild.start_date = new Date(taskObj.end_date);
                          taskChild.end_date =  gantt.calculateEndDate(taskObj.end_date, taskChild.duration, "hour");
                      }
                      gantt.updateTask(taskObj.id);
                      gantt.updateTask(taskChild.id);
                        gantt.eachTask(function(child){
                        var taskParentChild = gantt.getTask(child.parent);

                          child.start_date = gantt.roundDate(new Date(taskParentChild.end_date.valueOf() + diff));
                          child.end_date = gantt.calculateEndDate(child.start_date, child.duration);
                        gantt.refreshTask(child.id, true);
                        },taskObj.id );


                    }
              }
              }
            }
              gantt.refreshData();
            return true;
        });

        //Remet en place les tâches quand déplace via lightbox
        gantt.attachEvent("onAfterTaskUpdate", function(id,item){
            var taskObj = gantt.getTask(id);
            if (taskObj.parent != null && taskObj.parent > 0) {
                var taskParent = gantt.getTask(taskObj.parent);
                  if (taskObj.start_date <= taskParent.end_date) {
                    taskObj.start_date = new Date(taskParent.end_date);
                    taskObj.end_date =  gantt.calculateEndDate(taskParent.end_date, taskObj.duration, "hour");
                    //dhtmlx.message("Vous ne pouvez pas programmer une tâche avant sa tâche mère");
                   }
            }
            gantt.eachTask(function(child){

              var taskParentChild = gantt.getTask(child.parent);
              if (taskParentChild.end_date > child.start_date) {
                child.start_date = gantt.roundDate(new Date(taskParentChild.end_date.valueOf()));
                child.end_date = gantt.calculateEndDate(child.start_date, child.duration);
                gantt.refreshTask(child.id, true);
              }
                  },taskObj.id );
            return true;
          });

        //relier pere et fils quand une tache est effacée
        gantt.attachEvent("onBeforeTaskDelete", function(id,item){
            var taskObj = gantt.getTask(id);
            if (taskObj.parent != null && taskObj.parent > 0) {
                var taskParent = gantt.getTask(taskObj.parent);
                var tabchild = gantt.getChildren(taskObj.id);
                if (tabchild.length > 0 ) {
                    var taskChild = gantt.getTask(tabchild[0]);
                    taskChild.parent = taskParent.id;
                    taskChild.start_date = taskParent.start_end;
                    taskChild.end_date = gantt.calculateEndDate(taskChild.start_date, taskChild.duration);
                    gantt.refreshTask(taskChild.id, true);
                    gantt.eachTask(function(child){
                        taskParent.color = "orange";
                        var taskParentChild = gantt.getTask(child.parent);
                        child.start_date = gantt.roundDate(new Date(taskParentChild.end_date.valueOf()));
                        child.end_date = gantt.calculateEndDate(child.start_date, child.duration);
                        gantt.refreshTask(child.id, true);
                      },taskChild.id );

                }
            }
            return true;
        });


        var xml = "";
        gantt.attachEvent("onAfterTaskUpdate", function() {
            xml = gantt.serialize("xml");
            //window.alert(xml);
        });

        gantt.init("gantt_here");
        gantt.parse (tasks);

        </script>
        <g:hiddenField name="GanttData" value=""/>
        <g:actionSubmit class="btn btn-primary" action="saveGanttData"
                        value="Sauvegarder le Gantt"/>
    </g:if>
</g:form>


<table id="dateTable" class="table table-striped">
    <thead>
    <tr class="upper">
        <th style="text-align: center;">#</th>
        <th>type</th>
        <th style="display: none">code</th>
        <th>titre</th>
        <th>Intrigue</th>
        <th>Planifié</th>
        <th style="text-align: center;">date</th>
    </tr>
    </thead>



    <tbody>
    <g:each status="i" in="${pastsceneList}" var="pastscene">
        <tr id="pastscene${pastscene.id}_plot${pastscene.plotId}">
            <!-- # -->
            <td style="text-align: center;">${i + 1}</td>
            <!-- Type -->
            <th>Scène passée</th>
            <!-- Code - modal button -->
            <td style="display: none"><a href="#modalPas${i + 1}" role="button" class="btn" data-toggle="modal">PAS-${pastscene.id.encodeAsHTML()}_${pastscene.plotId.encodeAsHTML()}</a></td>
            <!-- Title -->
            <td>${pastscene.title.encodeAsHTML()}</td>
            <!-- Plot -->
            <td>${pastscene.plotName.encodeAsHTML()}</td>
            <!-- Is planned -->
            <td><span class="label label-info">OUI</span></td>
            <!-- Date -->
            <td style="text-align: center; font-weight: bold; font-size: 120%;" class="date" isEmpty="true"></td>
        </tr>
    </g:each>
    <g:each status="i" in="${eventList}" var="event">
        <tr id="event${event.id}_plot${event.plotId}">
            <!-- # -->
            <td style="text-align: center;">${pastsceneList.size() + i + 1}</td>
            <!-- Type -->
            <th>Événement</th>
            <!-- Code - modal button -->
            <td style="display: none"><a SDWhref="#modalEve${i + 1}" role="button" class="btn"  data-toggle="modal">EVE-${event.id.encodeAsHTML()}_${event.plotId.encodeAsHTML()}</a></td>
            <!-- Title -->
            <td>${event.name.encodeAsHTML()}</td>
            <!-- Plot -->
            <td>${event.plotName.encodeAsHTML()}</td>
            <!-- Is planned -->
            <td>
                <g:if test="${event.isPlanned}">
                    <span class="label label-info">OUI</span>
                </g:if>
                <g:else>
                    <span class="label label-warning">NON</span>
                </g:else></td>
            <!-- Date -->
            <td style="text-align: center; font-weight: bold; font-size: 120%;" class="date" isEmpty="false" ></td>
        </tr>
    </g:each>
    <tbody>
</table>

<!-- Modal Views -->
<!--g:render template="modalViewDates" /-->

<g:javascript src="substitution/subDates.js" />

<script type="text/javascript">
    $(document).ready(function() {
        // DatesJSON
        datesJSON = initDatesJSON();

        isSubDatesRunning = false;

        // Run dates substitution
        //runDatesSubstitution("${g.createLink(controller:'substitution', action:'getSubDates')}");
        initDateList("${g.createLink(controller:'substitution', action:'getSubDates', params: [subDates : params.subDates] )}");
    });

    function initDatesJSON() {
        var jsonObject = new Object();

        // Begin gn date
        jsonObject.beginDate = "${gnInfo.t0Date.format('yyyy.MM.dd HH:mm')}";
        // Duration
        jsonObject.duration = "${gnInfo.duration}";

        // BEGIN Pastscenes LOOP
        var pastsceneArray = new Array();
        <g:each status="i" in="${pastsceneList}" var="pastscene">
            var pastscene = new Object();
            // Gn id
            pastscene.gnId = "${pastscene.id}";
            // Gn plot id
            pastscene.gnPlotId = "${pastscene.plotId}";
            // HTML id
            pastscene.htmlId = "pastscene${pastscene.id}_plot${pastscene.plotId}";
            // Time

            // DOIT ËTRE SUPP
            //pastscene.relativeTime = "${pastscene.relativeTime}";
            //pastscene.relativeTimeUnit = "${pastscene.relativeTimeUnit}";
            // FIN DOIT ETRE SUPP
            pastscene.absoluteYear = "${pastscene.absoluteYear}";
            pastscene.absoluteMonth = "${pastscene.absoluteMonth}";
            pastscene.absoluteDay = "${pastscene.absoluteDay}";
            pastscene.absoluteHour = "${pastscene.absoluteHour}";
            pastscene.absoluteMinute = "${pastscene.absoluteMinute}";



            pastscene.isYearAbsolute = "${pastscene.isYearAbsolute}";
            pastscene.isMonthAbsolute = "${pastscene.isMonthAbsolute}";
            pastscene.isDayAbsolute = "${pastscene.isDayAbsolute}";
            pastscene.isHourAbsolute = "${pastscene.isHourAbsolute}";
            pastscene.isMinuteAbsolute = "${pastscene.isMinuteAbsolute}";

            pastscene.isUpdate = "";
            pastsceneArray.push(pastscene);
        </g:each>
        // END Pastscenes LOOP
        jsonObject.pastscenes = pastsceneArray;

        // BEGIN Events LOOP
        var eventArray = new Array();
        <g:each status="i" in="${eventList}" var="event">
        var event = new Object();
        // Gn id
        event.gnId = "${event.id}";
        // Gn plot id
        event.gnPlotId = "${event.plotId}";
        // HTML id
        event.htmlId = "event${event.id}_plot${event.plotId}";
        eventArray.push(event);
        // Time
        event.timing = "${event.timing}";
        </g:each>
        // END Events LOOP
        jsonObject.events = eventArray;
        return jsonObject;
    }

</script>

