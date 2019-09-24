package com.aurei.quanyi.utils

import java.io.Serializable

/**
 * @author yudneghao
 * @date 2019-09-24
 */
class BaiDuStartWebBean : Serializable {


    /**
     * action : onEventWithAttributes
     * obj : {"event_id":"homepage","label":{},"attributes":{}}
     */

    var action: String? = null
    var obj: Data? = null


    inner class Data : Serializable {
        /**
         * event_id : homepage
         * label : {}
         * attributes : {}
         */

        var event_id: String? = null
        var label: Any? = null
        var attributes: Any? = null


    }
}
