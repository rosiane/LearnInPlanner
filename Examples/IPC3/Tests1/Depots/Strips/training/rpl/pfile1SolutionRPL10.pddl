1
(Drop hoist1 crate1 pallet1 distributor0)
0
(:init
	(lifting hoist1 crate1)
	(at pallet1 distributor0)
	(at crate0 distributor1)
	(at hoist0 depot0)
	(at hoist1 distributor0)
	(at hoist2 distributor1)
	(clear crate0)
	(at pallet2 distributor1)
	(available hoist0)
	(on crate0 pallet2)
	(available hoist2)
	(clear pallet1)
	(at truck0 distributor1)
	(at pallet0 depot0)
	(at truck1 distributor1)
	(clear pallet0)
)
(:add
	(available hoist1)
	(at crate1 distributor0)
	(on crate1 pallet1)
	(clear crate1)
)
(:delete
	(lifting hoist1 crate1)
	(clear pallet1)
)
(:goal
	(on crate1 pallet1)
	(on crate0 pallet2)
)
(:current
	(on crate0 pallet2)
)
