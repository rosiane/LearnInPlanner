9
(Drive truck1 distributor1 depot0)
(Load hoist2 crate1 truck1 distributor1)
(Drive truck1 distributor1 distributor0)
(Unload hoist1 crate1 truck1 distributor0)
(Unload hoist2 crate0 truck1 distributor1)
(Unload hoist0 crate2 truck1 depot0)
(Drop hoist1 crate1 crate3 distributor0)
(Drop hoist0 crate2 pallet0 depot0)
(Drop hoist2 crate0 pallet2 distributor1)
0
(:init
	(on crate1 pallet0)
	(clear pallet0)
	(at truck1 distributor1)
	(at pallet0 depot0)
	(clear pallet2)
	(on crate3 pallet1)
	(at crate1 depot0)
	(at hoist2 distributor1)
	(available hoist1)
	(at hoist0 depot0)
	(in crate0 truck1)
	(at pallet1 distributor0)
	(at crate0 distributor0)
	(on crate0 pallet1)
	(in crate2 truck1)
	(at truck0 depot0)
	(at crate3 distributor0)
	(at truck0 distributor1)
	(available hoist0)
	(clear crate3)
	(at pallet2 distributor1)
	(lifting hoist2 crate1)
	(at hoist1 distributor0)
)
(:add
	(lifting hoist2 crate0)
	(at truck1 depot0)
	(at crate1 distributor0)
	(on crate1 crate3)
	(available hoist1)
	(clear crate1)
	(available hoist2)
	(in crate1 truck1)
	(clear crate0)
	(at crate0 distributor1)
	(available hoist2)
	(on crate0 pallet2)
	(lifting hoist0 crate2)
	(lifting hoist1 crate1)
	(clear crate2)
	(at crate2 depot0)
	(available hoist0)
	(on crate2 pallet0)
	(at truck1 distributor0)
)
(:delete
	(in crate0 truck1)
	(available hoist2)
	(at truck1 distributor1)
	(lifting hoist1 crate1)
	(clear crate3)
	(lifting hoist2 crate1)
	(lifting hoist2 crate0)
	(clear pallet2)
	(available hoist0)
	(in crate2 truck1)
	(available hoist1)
	(in crate1 truck1)
	(clear pallet0)
	(lifting hoist0 crate2)
	(at truck1 distributor1)
)
(:goal
	(on crate3 pallet1)
	(on crate0 pallet2)
	(on crate1 crate3)
	(on crate2 pallet0)
)
(:current
	(on crate3 pallet1)
)
