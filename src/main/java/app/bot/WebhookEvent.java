/*
 * Copyright (C) 2018 mjamelle
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package app.bot;

import java.net.URI;
import java.util.Date;
import lombok.*;

/**
 *
 * @author mjamelle
 */

@Data
public class WebhookEvent {
    private String id;
    private String name;
    private URI targetUrl;
    private String resource;
    private String event;
    private String orgId;
    private String createdBy;
    private String appId;
    private String ownedBy;
    private String status;
    private Date created;
    private String actorId;
    private HookData data;
}
