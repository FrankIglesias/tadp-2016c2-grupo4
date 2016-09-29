class Module
  def uninclude(mod)
    mod.instance_methods.each do |method|
      undef_method(method)
    end
  end
end