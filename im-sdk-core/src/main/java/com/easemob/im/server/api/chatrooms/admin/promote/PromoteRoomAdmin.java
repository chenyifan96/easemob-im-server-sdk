package com.easemob.im.server.api.chatrooms.admin.promote;

import com.easemob.im.server.api.Context;
import com.easemob.im.server.exception.EMUnknownException;
import reactor.core.publisher.Mono;

public class PromoteRoomAdmin {

    public static Mono<Void> single(Context context, String roomId, String username) {
        return context.getHttpClient()
                .post()
                .uri(String.format("/chatrooms/%s/admin", roomId))
                .send(Mono.create(sink -> sink.success(context.getCodec().encode(new PromoteRoomAdminRequest(username)))))
                .responseSingle((rsp, buf) -> context.getErrorMapper().apply(rsp).then(buf))
                .map(buf -> context.getCodec().decode(buf, PromoteRoomAdminResponse.class))
                .handle((rsp, sink) -> {
                    if (!rsp.isSuccess()) {
                        sink.error(new EMUnknownException("unknown"));
                        return;
                    }
                    sink.complete();
                });
    }

}
