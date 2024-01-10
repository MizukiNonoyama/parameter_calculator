package parameter_calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import parameter_calculator.config.Config;
import parameter_calculator.neural_network.Processor;
import parameter_calculator.utils.MathHelper;

public class Prototype {
	
	public List<List<Double>> buffer;
	public List<List<Double>> buffer_generated_commands;
	public final Processor p;
	private final int mov_avg_range;
	private final double delay;
	private final double avg;
	private final double std;
	private final double robot_radius;
	
	public Prototype(Processor p,int buffer_size, int vision_delay_cycle, int mov_avg_range, double delay, double robot_radius) {
		this.buffer = new ArrayList<List<Double>>();
		this.buffer_generated_commands = new ArrayList<List<Double>>();
		for(int i = 0;i < buffer_size;i++) this.buffer.add(Arrays.asList(new Double [] {0.0,0.0,0.0,0.0,0.0,0.0}));
		for(int i = 0;i < vision_delay_cycle;i++) this.buffer_generated_commands.add(Arrays.asList(new Double [] {0.0,0.0,0.0}));
		this.mov_avg_range = mov_avg_range;
		this.delay = delay;
		this.avg = p.getAvg();
		this.std = p.getStd();
		this.robot_radius = robot_radius;
		this.p = p;
	}
	
	/**
	 * @param d : vx_command, vy_command, omega_command, vx_feedback, vy_feedback, omega_feedback
	 */
	public void read(double[] d) {
		this.buffer = new ArrayList<List<Double>>(this.buffer.subList(1, this.buffer.size()));
		List<Double> input = Arrays.stream(d).boxed().collect(Collectors.toList());
		input.set(2, input.get(2) * this.robot_radius);
		input.set(5, input.get(5) * this.robot_radius);
		this.buffer.add(Arrays.stream(d).boxed().collect(Collectors.toList()));
	}
	
	/**
	 * @param d : vx_command, vy_command, omega_command
	 */
	public void read_test(double[] d) {
		this.buffer = new ArrayList<List<Double>>(this.buffer.subList(1, this.buffer.size()));
		List<Double> input = Arrays.stream(d).boxed().collect(Collectors.toList());
		input.addAll(this.buffer_generated_commands.get(0));
		input.set(2, input.get(2) * this.robot_radius);
		input.set(5, input.get(5) * this.robot_radius);
		this.buffer.add(input);
	}
	
	/**
	 * @param d : vx_command, vy_command, omega_command (generated)
	 */
	public void write_generated_commands(double[] d) {
		this.buffer_generated_commands = new ArrayList<List<Double>>(this.buffer_generated_commands.subList(1, this.buffer_generated_commands.size()));
		this.buffer_generated_commands.add(Arrays.stream(d).boxed().collect(Collectors.toList()));
	}
	
	public List<Double> makeSample() {
		List<Double> list = new ArrayList<Double>();
		for(int i = 0;i < this.buffer.size();i++) {
			List<Double> low = new ArrayList<Double>();
			low.addAll(this.buffer.get(i));
			if(i < this.mov_avg_range || !Config.useAcc) {
				low.addAll(Arrays.asList(new Double[] {0.0,0.0,0.0,0.0,0.0,0.0}));
			}
			else {
				for(int j = 0;j < 6;j++) {
					double acc = this.buffer.get(i).get(j) - this.buffer.get(i - this.mov_avg_range).get(j);
					acc /= (double)this.mov_avg_range;
					acc /= this.delay;
					low.add(acc);
				}
			}
			list.addAll(low);
		}
		
		List<Double> gaussian = new ArrayList<Double>();
		for(double d : list) {
			gaussian.add(MathHelper.getStandardization(d, this.avg, this.std));
		}
		
		return gaussian;
	}
	
	public List<Double> generate() {
		List<Double> d = this.p.generateCommand(this.makeSample(), this.std, this.avg, this.robot_radius);
		this.write_generated_commands(new double[] {d.get(0), d.get(1), d.get(2)});
		return d;
	}
}
