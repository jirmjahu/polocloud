/*
 * Copyright 2024 Mirco Lindenau | HttpMarco
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.httpmarco.polocloud.proxy.platform.velocity.configuration.maintenance;

import dev.httpmarco.polocloud.proxy.config.maintenance.Maintenance;
import dev.httpmarco.polocloud.proxy.config.maintenance.MaintenanceProvider;
import dev.httpmarco.polocloud.proxy.platform.velocity.configuration.Configuration;

public class VelocityMaintenanceProvider implements MaintenanceProvider {

    public final static String VERSION_NAME = "§4§lMaintenance";
    public final static String MOTD = "<gradient:#00fdee:#118bd1><bold>PoloCloud</bold></gradient> <dark_gray>» <white>github.com/HttpMarco/polocloud\n<red><bold>CURRENTLY UNDER MAINTENANCE</bold>";
    private final Maintenance maintenance;

    public VelocityMaintenanceProvider() {
        this.maintenance = Configuration.loadOrCreateDefault().getMaintenance();
    }

    @Override
    public Maintenance maintenance() {
        return this.maintenance;
    }
}
