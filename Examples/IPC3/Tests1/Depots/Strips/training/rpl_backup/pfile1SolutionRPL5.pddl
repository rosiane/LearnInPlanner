7
(Load hoist1 crate0 truck1 distributor0)
(Drive truck1 distributor0 distributor1)
(Drop hoist1 crate0 pallet1 distributor0)
(Unload hoist2 crate0 truck1 distributor1)
(Unload hoist1 crate1 truck1 distributor0)
(Drop hoist1 crate1 pallet1 distributor0)
(Drop hoist2 crate0 pallet2 distributor1)
-1
(:init
	(clear pallet2)
	(at pallet1 distributor0)
	(lifting hoist1 crate0)
	(at truck1 distributor0)
	(at hoist0 depot0)
	(at hoist1 distributor0)
	(at hoist2 distributor1)
	(at pallet2 distributor1)
	(available hoist0)
	(in crate1 truck1)
	(clear pallet1)
	(available hoist2)
	(at pallet0 depot0)
	(at truck0 distributor1)
	(clear pallet0)
)
(:add
	(lifting hoist2 crate0)
	(available hoist1)
	(in crate0 truck1)
	(clear crate0)
	(on crate0 pallet2)
	(available hoist2)
	(at crate0 distributor1)
	(at truck1 distributor1)
	(lifting hoist1 crate1)
	(available hoist1)
	(clear crate0)
	(at crate0 distributor0)
	(on crate0 pallet1)
	(available hoist1)
	(at crate1 distributor0)
	(on crate1 pallet1)
	(clear crate1)
)
(:delete
	(in crate0 truck1)
	(available hoist2)
	(lifting hoist1 crate0)
	(clear pallet2)
	(lifting hoist2 crate0)
	(at truck1 distributor0)
	(available hoist1)
	(in crate1 truck1)
	(clear pallet1)
	(lifting hoist1 crate0)
	(lifting hoist1 crate1)
	(clear pallet1)
)
(:goal
	(on crate1 pallet1)
	(on crate0 pallet2)
)
(:current
)
