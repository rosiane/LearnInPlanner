14
(Load hoist1 crate3 truck0 distributor0)
(Drive truck0 distributor0 depot0)
(Lift hoist0 crate0 pallet1 depot0)
(Drive truck0 distributor0 distributor1)
(Lift hoist0 crate2 crate1 depot0)
(Load hoist0 crate0 truck0 depot0)
(Unload hoist1 crate4 truck0 distributor0)
(Unload hoist1 crate5 truck1 distributor0)
(Unload hoist0 crate3 truck0 depot0)
(Drop hoist1 crate4 pallet1 distributor0)
(Unload hoist2 crate0 truck0 distributor1)
(Drop hoist0 crate3 crate2 depot0)
(Drop hoist1 crate5 crate0 distributor0)
(Drop hoist2 crate0 crate1 distributor1)
6
(:init
	(on crate0 pallet1)
	(clear crate2)
	(on crate2 pallet0)
	(at truck0 distributor0)
	(in crate4 truck0)
	(clear pallet2)
	(on crate2 crate1)
	(at hoist0 depot0)
	(at pallet0 depot0)
	(available hoist2)
	(on crate1 pallet2)
	(lifting hoist1 crate3)
	(at crate0 distributor0)
	(at pallet1 distributor0)
	(at crate0 depot0)
	(in crate1 truck0)
	(at hoist1 distributor0)
	(at pallet2 distributor1)
	(clear crate0)
	(in crate5 truck1)
	(on crate0 pallet0)
	(at truck1 distributor0)
	(at crate1 distributor1)
	(at hoist2 distributor1)
	(on crate3 pallet1)
	(available hoist0)
	(at crate2 depot0)
)
(:add
	(at truck0 depot0)
	(at truck0 distributor1)
	(clear crate3)
	(available hoist0)
	(at crate3 depot0)
	(on crate3 crate2)
	(lifting hoist0 crate3)
	(available hoist1)
	(on crate5 crate0)
	(clear crate5)
	(at crate5 distributor0)
	(lifting hoist1 crate5)
	(available hoist1)
	(in crate3 truck0)
	(in crate0 truck0)
	(available hoist0)
	(lifting hoist0 crate0)
	(clear pallet1)
	(available hoist1)
	(at crate4 distributor0)
	(on crate4 pallet1)
	(clear crate4)
	(lifting hoist1 crate4)
	(lifting hoist2 crate0)
	(lifting hoist0 crate2)
	(clear crate1)
	(on crate0 crate1)
	(clear crate0)
	(available hoist2)
	(at crate0 distributor1)
)
(:delete
	(at truck0 distributor0)
	(at truck0 distributor0)
	(clear crate2)
	(lifting hoist0 crate3)
	(in crate3 truck0)
	(available hoist0)
	(clear crate0)
	(lifting hoist1 crate5)
	(available hoist1)
	(in crate5 truck1)
	(lifting hoist1 crate3)
	(lifting hoist0 crate0)
	(on crate0 pallet1)
	(clear crate0)
	(available hoist0)
	(at crate0 depot0)
	(clear pallet1)
	(lifting hoist1 crate4)
	(available hoist1)
	(in crate4 truck0)
	(in crate0 truck0)
	(available hoist2)
	(clear crate2)
	(available hoist0)
	(on crate2 crate1)
	(at crate2 depot0)
	(lifting hoist2 crate0)
	(clear crate1)
)
(:goal
	(on crate5 crate0)
	(on crate0 crate1)
	(on crate4 pallet1)
	(on crate2 pallet0)
	(on crate1 pallet2)
	(on crate3 crate2)
)
(:current
	(on crate2 pallet0)
	(on crate1 pallet2)
)
