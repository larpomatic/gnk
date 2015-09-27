package org.gnk.resplacetime


class GnConstantController {



    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [gnConstantInstanceList: GnConstant.list(params), gnConstantInstanceTotal: GnConstant.count()]
    }

    def create() {
        [genericPlaceInstance: new GenericPlace(params)]
    }

    def save() {
        GnConstant gnConstant = new GnConstant(params);
        Boolean res = saveOrUpdate(gnConstant);
//        genericPlace = GenericPlace.findAllWhere("code": genericPlace.getCode(), "plot": ).first();
    }


    def saveOrUpdate(GnConstant newGnConstant) {
//        Plot plot = null;
        if (params.containsKey("name")) {
            newGnConstant.name = params.name;
        } else {
            return false
        }
        if (params.containsKey("constantType")) {
            newGnConstant.constantType = params.constantType;
        } else {
            return false
        }

        newGnConstant.save(flush: true);
//        newGenericPlace = GenericPlace.findAllWhere("code": newGenericPlace.getCode(), "plot": plot).first();

        newGnConstant.save(flush: true);
        return true;
    }

    def show(Long id) {
        def gnConstantInstance = GnConstant.get(id)
        if (!gnConstantInstance) {
           // flash.message = message(code: 'default.not.found.message', args: [message(code: 'gnConstant.label', default: 'GnConstant'), id])
            redirect(action: "list")
            return
        }

        [gnConstantInstance: gnConstantInstance]
    }

    def edit(Long id) {
        def gnConstantInstance = GnConstant.get(id)
        if (!gnConstantInstance) {
            //flash.message = message(code: 'default.not.found.message', args: [message(code: 'gnConstant.label', default: 'GnConstant'), id])
            redirect(action: "list")
            return
        }

        [gnConstantInstance: gnConstantInstance]
    }

    def update(Long id) {
        GnConstant gnConstant = GnConstant.get(id);
        String oldname = gnConstant.name;
        if (gnConstant) {
            render(contentType: "application/json") {
                object(isupdate: saveOrUpdate(gnConstant),
                        id: gnConstant.id,
                        name: gnConstant.name,
                        oldname: oldname)
            }
        }
    }

    def delete(Long id) {
        def gnConstantInstance = GnConstant.get(id)
        boolean isDelete = false;
        if (gnConstantInstance) {
            gnConstantInstance.delete(flush: true)
            isDelete = true;
        }

        render(contentType: "application/json") {
            object(isdelete: isDelete)
        }
    }
}
