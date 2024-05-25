package data.missions;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI.ShipPickMode;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.campaign.FleetInflater;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.fleets.DefaultFleetInflater;
import com.fs.starfarer.api.impl.campaign.fleets.DefaultFleetInflaterParams;
import com.fs.starfarer.api.impl.campaign.fleets.FleetFactoryV3;
import com.fs.starfarer.api.impl.campaign.fleets.FleetParamsV3;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.mission.MissionDefinitionAPI;
import data.scripts.SWPModPlugin;
import data.scripts.campaign.II_IGFleetInflater;
import java.util.Random;
import org.lwjgl.util.vector.Vector2f;

public interface SWP_FleetGenerator {

    static enum GeneratorFleetTypes {

        RAIDERS(new RaidersGen()),
        PATROL(new PatrolGen()),
        HUNTERS(new HuntersGen()),
        WAR(new WarGen()),
        DEFENSE(new DefenseGen()),
        CONVOY(new ConvoyGen()),
        BLOCKADE(new BlockadeGen()),
        INVASION(new InvasionGen());

        private final SWP_FleetGenerator gen;

        private GeneratorFleetTypes(SWP_FleetGenerator gen) {
            this.gen = gen;
        }

        FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            return gen.generate(api, side, faction, qf, opBonus, avgSMods, maxPts, seed, autoshit);
        }

        static FleetInflater pickInflater(String factionId, Object p) {
            FleetInflater inflater;
            if (SWPModPlugin.imperiumExists && factionId.contentEquals("ii_imperial_guard")) {
                inflater = new II_IGFleetInflater((DefaultFleetInflaterParams) p);
            } else {
                inflater = new DefaultFleetInflater((DefaultFleetInflaterParams) p);
            }
            return inflater;
        }
    }

    FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit);

    static class RaidersGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 4);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts * 0.75f, // combatPts
                    maxPts * 0.25f, // freighterPts
                    0f, // tankerPts
                    0f, // transportPts
                    0f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }

    static class PatrolGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 4);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts, // combatPts
                    0f, // freighterPts
                    0f, // tankerPts
                    0f, // transportPts
                    0f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }

    static class HuntersGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 5);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts, // combatPts
                    0f, // freighterPts
                    0f, // tankerPts
                    0f, // transportPts
                    0f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }

    static class WarGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 6);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts, // combatPts
                    0f, // freighterPts
                    0f, // tankerPts
                    0f, // transportPts
                    0f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }

    static class DefenseGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 6);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts, // combatPts
                    0f, // freighterPts
                    0f, // tankerPts
                    0f, // transportPts
                    0f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }

    static class ConvoyGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 4);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts * 0.6f, // combatPts
                    maxPts * 0.1f, // freighterPts
                    maxPts * 0.1f, // tankerPts
                    maxPts * 0.1f, // transportPts
                    maxPts * 0.1f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }

    static class BlockadeGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 5);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts * 0.8f, // combatPts
                    maxPts * 0.1f, // freighterPts
                    0f, // tankerPts
                    maxPts * 0.1f, // transportPts
                    0f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }

    static class InvasionGen implements SWP_FleetGenerator {

        @Override
        public FleetDataAPI generate(MissionDefinitionAPI api, FleetSide side, String faction, float qf, float opBonus, int avgSMods, int maxPts, long seed, boolean autoshit) {
            MarketAPI market = Global.getFactory().createMarket("fake", "fake", 6);
            market.getStability().modifyFlat("fake", 10000);
            market.setFactionId(faction);
            SectorEntityToken token = Global.getSector().getHyperspace().createToken(0, 0);
            market.setPrimaryEntity(token);
            market.getStats().getDynamic().getMod(Stats.FLEET_QUALITY_MOD).modifyFlat("fake", FleetFactoryV3.BASE_QUALITY_WHEN_NO_MARKET);
            market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).modifyFlat("fake", 1f);
            FleetParamsV3 params = new FleetParamsV3(market,
                    new Vector2f(0, 0),
                    faction,
                    qf, // qualityOverride
                    "missionFleet",
                    maxPts * 0.7f, // combatPts
                    0f, // freighterPts
                    0f, // tankerPts
                    maxPts * 0.3f, // transportPts
                    0f, // linerPts
                    0f, // utilityPts
                    0f); // qualityMod
            params.withOfficers = false;
            params.ignoreMarketFleetSizeMult = true;
            params.forceAllowPhaseShipsEtc = true;
            params.modeOverride = ShipPickMode.PRIORITY_THEN_ALL;
            params.random = new Random(seed);
            params.averageSMods = avgSMods;

            CampaignFleetAPI fleetEntity = FleetFactoryV3.createFleet(params);
            if (fleetEntity == null) {
                return null;
            }

            DefaultFleetInflaterParams p = new DefaultFleetInflaterParams();
            p.quality = qf;
            p.seed = seed;
            p.mode = ShipPickMode.PRIORITY_THEN_ALL;
            p.allWeapons = !autoshit;
            p.averageSMods = avgSMods;
            p.factionId = faction;

            GeneratorFleetTypes.pickInflater(faction, p).inflate(fleetEntity);

            return SWP_BaseRandomBattle.finishFleet(fleetEntity.getFleetData(), side, faction, api);
        }
    }
}
